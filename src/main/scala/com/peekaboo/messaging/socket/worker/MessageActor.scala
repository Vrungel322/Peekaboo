package com.peekaboo.messaging.socket.worker

import java.io._
import java.util.UUID

import akka.actor.{Actor, ActorRef, Props}
import com.peekaboo.messaging.file.FileActor
import com.peekaboo.messaging.socket.middleware.BinaryMessageInterceptor
import org.apache.logging.log4j.LogManager
import org.springframework.web.socket.{BinaryMessage, WebSocketSession}

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.ExecutionContext.Implicits.global

class MessageActor(private val socket: WebSocketSession) extends Actor {


//  @scala.throws[Exception](classOf[Exception])
  override def postStop(): Unit = {

    //here might be a possibility that connection with socket wasn't closed. Have to do it manually
    logger.debug("Check if socket is open")
    if (socket.isOpen) {
      logger.debug("Closing connection with socket")
      socket.close()
    } else {
      logger.debug("Socket is closed. Nothing to do here")
    }
  }

  def sendToFileActor(id: String, author: String, data: Array[Byte]): Unit = {
    val resolver = ActorSystems.fileSystem.actorSelection(s"/user/$id").resolveOne(FiniteDuration(1, "s"))
    resolver.onSuccess {
      case actor =>
        logger.debug("Found existing actor")
        actor ! data
    }
    resolver.onFailure {
      case _ =>
        logger.debug("Creating another one actor")
        ActorSystems.fileSystem.actorOf(Props(new FileActor(id, generateFileName, author)), s"$id")
//        Thread.sleep(10)
        sendToFileActor(id, author, data)
    }


//    if (option.nonEmpty) {
//      val aTry = option.get
//      if (aTry.isSuccess) {
//        logger.debug("Found existing actor")
//        aTry.get
//      } else {
//        logger.debug("Created new one file actor")
//        ActorSystems.fileSystem.actorOf(Props(new FileActor(id, generateFileName, author)), s"$id")
//      }
//    } else ActorSystems.fileSystem.actorOf(Props(new FileActor(id, generateFileName, author)), s"$id")
  }

  def closeFile(id: String) = {
    ActorSystems.fileSystem.actorSelection(s"/user/$id").resolveOne(FiniteDuration(1, "s")).onSuccess {
      case actor =>
        logger.debug("Closing file actor")
        ActorSystems.fileSystem.stop(actor)
    }
  }

  def receive = {
    case (msg: Send, id: String) =>
//      receiveTextMessage(msg)

      if (msg.getType == Type.Audio) {

        if (msg.getParameter(ParameterName.Reason).nonEmpty && msg.getParameter(ParameterName.Reason).get == "end") {
          logger.debug("Got last piece. Closing file.")
          closeFile(msg.getDestination)
        } else {
          logger.debug("Got audio message")
          sendToFileActor(msg.getDestination, id, msg.getBody)
        }

      } else {
        val action = msg.toMessage(id) //Message(msg.getBody, Map(ParameterName.From -> id))
        sendMessage(action)
      }
    case f: FileAction =>
      splitFile(f.name, f.author).foreach(sendMessage)
    case a =>
      logger.debug("Message received but it cannot be resolved")
      logger.debug(a)
  }

  private def splitFile(fileName: String, id: String): Iterator[Send] = {

    val reader = new FileInputStream(new File(fileName))
    val acc = new Array[Byte](reader.available())
    reader.read(acc)

    val header = new Header("SEND", Map(ParameterName.From -> id, ParameterName.Type -> Type.Audio))
    val size = header.size
    val limit = socket.getBinaryMessageSizeLimit
    acc.grouped(limit - size).map(payload => header.toSend(payload))
  }

//  private def receiveTextMessage(message: Send): Unit = {
//    sendTextMessage(message)
//  }

  private def generateFileName: String = UUID.randomUUID.toString

  private def sendMessage(message: Action): Unit = {
    val msg = BinaryMessageInterceptor.compose(message)
    logger.debug("Message:\n" + new String(msg))
    socket.sendMessage(new BinaryMessage(msg))
  }

//  private def receiveAudioMessage(message: Audio): Unit = {
//  }
//
//  private def sendAudioMessage(message: Audio): Unit = {
//  }

  private val logger = LogManager.getLogger(MessageActor.this)
}
