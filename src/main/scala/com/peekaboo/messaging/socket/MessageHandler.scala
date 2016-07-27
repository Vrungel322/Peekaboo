package com.peekaboo.messaging.socket

import akka.actor.{ActorSystem, Props}
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.socket.handler.BinaryWebSocketHandler
import org.springframework.web.socket.{TextMessage, _}

import scala.collection.JavaConverters._

@Autowired
class MessageHandler extends BinaryWebSocketHandler {


  override def handleBinaryMessage(session: WebSocketSession, message: BinaryMessage): Unit = {
    try {
      logger.debug("Got message")
      val action = MessageInterceptor.handle(message.getPayload.array())
      logger.debug("Action was successfully parsed. Fetching user id")
      val ownerId = session.getHandshakeHeaders.get("id").get(0)
      logger.debug(s"User id is ${ownerId}")
      RequestDispatcher.process(action, ownerId)
    } catch {
      case e: IllegalArgumentException => {
        logger.warn("Error. Cannot parse" + new String(message.getPayload.array()))
      }
    }

  }

  val actorSystem = MessageActorSystem.system

  override def afterConnectionEstablished(session: WebSocketSession): Unit = {
    logger.debug("Connection established.")
    val id = session.getHandshakeHeaders.get("id").get(0)
//    logger.debug("Attempting to find existing actor")

    //    val actor = new MessageActor(session)

//    actorSystem.actorOf(Props(classOf[MessageActor], session), id)
    val actRef = actorSystem.actorOf(Props(new MessageActor(session)), id)
    logger.debug("Created actor " + actRef.toString())
    session.sendMessage(new PongMessage())

//
//    if (ActorPool.findActor(id).isEmpty) {
//      logger.debug("There is no actor for user id: " + id)
//      logger.debug("Creating new actor")
////      ActorPool.addActor(id, actor)
//    } else {
//      logger.debug(s"Found actor for user id: ${id}")
//      logger.debug("Updating existing actor")
////      ActorPool.updateActor(id, actor)
//    }

  }


  override def handleTransportError(session: WebSocketSession, throwable: Throwable): Unit = {
    val response =
      MessageHandler.om.
        writeValueAsString(
          Map(
            "type" -> "transport error",
            "message" -> "what the hack is this error?"
          ).asJava
        )
    session.sendMessage(new TextMessage(response))
  }


  //  override def handleMessage(session: WebSocketSession, message: WebSocketMessage[_]): Unit = {
  //    val headers = session.getHandshakeHeaders
  //    val optionalUser = authenticate(headers)
  //    if (optionalUser.isEmpty) {
  //      logger.debug("User are not authenticated")
  //      val response =
  //        MessageHandler.om.
  //          writeValueAsString(
  //            Map(
  //              "type" -> "send error",
  //              "message" -> "Cannot send due to absence of authorization header"
  //            ).asJava
  //          )
  //      session.sendMessage(new TextMessage(response))
  //    } else {
  //      val receiver = headers.getFirst("Receiver")
  //      val senderUser = optionalUser.get
  //      val msg = message match {
  //        case message: TextMessage => Text(message.getPayload, receiver, senderUser.getId)
  //        case message: BinaryMessage =>
  //      }
  //      val actor = actorPool.findActor(receiver)
  //      actor ! msg
  //    }
  //    println(new String(message.asInstanceOf[BinaryMessage].getPayload.array()))
  //  }

  private val logger = LogManager.getLogger(MessageHandler.this)

  override def afterConnectionClosed(webSocketSession: WebSocketSession, closeStatus: CloseStatus): Unit = {

  }

  override def supportsPartialMessages(): Boolean = true

}

object MessageHandler {
  val om = new ObjectMapper()
}