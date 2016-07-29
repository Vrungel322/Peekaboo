package com.peekaboo.messaging.socket

import akka.actor.Props
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.socket._
import org.springframework.web.socket.handler.BinaryWebSocketHandler

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

@Autowired
class MessageHandler extends BinaryWebSocketHandler {


  override def handleBinaryMessage(session: WebSocketSession, message: BinaryMessage): Unit = {
    try {
      logger.debug("Got message")
      val action = MessageInterceptor.handle(message.getPayload.array())
      logger.debug("Action was successfully parsed. Fetching user id")
      val ownerId = session.getHandshakeHeaders.get("id").get(0)
      RequestDispatcher.process(action, ownerId)
    } catch {
      case e: IllegalArgumentException => {
        logger.warn("Error. Cannot parse" + new String(message.getPayload.array()))
      }
    }

  }

  private val actorSystem = MessageActorSystem.system

  override def afterConnectionEstablished(session: WebSocketSession): Unit = {

    logger.debug("Connection established.")
    val id = session.getHandshakeHeaders.get("id").get(0)

    //at first it looks if there was connection with client
    actorSystem.actorSelection(s"/user/${id}").resolveOne(FiniteDuration(1, "s")).onComplete(a => {
      logger.debug("Future has been reached")

      //if the connection existed stop actor handling it
      if (a.isSuccess) {
        logger.debug("Future is success")
        logger.debug("Removing actor")
        actorSystem.stop(a.get)
      }

      //after one second create new actor with connection
      val future = Future {
        logger.debug("Creating new actor after 1 second")
        Thread.sleep(1000)
        logger.debug("1 second has been passed")
        actorSystem.actorOf(Props(new MessageActor(session)), id)
      }

      //here actor created send pong message to check it
      future.onComplete(actRef => {
        logger.debug("Created actor " + actRef.get.toString())
        session.sendMessage(new PongMessage())
      })


    })

  }

  override def handleTransportError(session: WebSocketSession, throwable: Throwable): Unit = {
//    val response =
//      MessageHandler.om.
//        writeValueAsString(
//          Map(
//            "type" -> "transport error",
//            "message" -> "what the hack is this error?"
//          ).asJava
//        )
//    session.sendMessage(new TextMessage(response))
  }

  private val logger = LogManager.getLogger(MessageHandler.this)

  override def afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus): Unit = {

    logger.debug("Closing connection has been reached")

    val id = session.getHandshakeHeaders.get("id").get(0)
    actorSystem.actorSelection(s"/user/${id}").resolveOne(FiniteDuration(1, "s")).onComplete(a => {
      logger.debug("Future has been reached")
      if (a.isSuccess) {
        logger.debug("Future is success")
        logger.debug("Removing actor")
        actorSystem.stop(a.get)
      }
    })
  }

  override def supportsPartialMessages(): Boolean = true

}
