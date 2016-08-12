package com.peekaboo.messaging.socket.middleware

import akka.actor.Props
import com.peekaboo.messaging.socket.worker.{MessageActor, ActorSystems}
import org.apache.logging.log4j.LogManager
import org.springframework.web.socket._
import org.springframework.web.socket.handler.BinaryWebSocketHandler

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration


class MessageHandler(requestDispatcher: RequestDispatcher, messageInterceptor: MessageInterceptor) extends BinaryWebSocketHandler {


  override def handleBinaryMessage(session: WebSocketSession, message: BinaryMessage): Unit = {
    try {
      logger.debug("Message size: " + message.getPayloadLength)
//      logger.debug("First 100 symbols of message:\n" + new String(message.getPayload.array().splitAt(100)._1))
      val action = messageInterceptor.handle(message.getPayload.array())
//      logger.debug("Action was successfully parsed. Fetching user id")
      val ownerId = getId(session)
//      val optionalProcessing = requestDispatcher.process(action, ownerId)
      requestDispatcher.process(action, ownerId)


    }
    catch {
      case e: IllegalArgumentException => {
        logger.warn("Error. Cannot parse" + new String(message.getPayload.array()))
      }
    }

  }

  private val actorSystem = ActorSystems.messageSystem

  override def afterConnectionEstablished(session: WebSocketSession): Unit = {

    val id = getId(session)
    session.setBinaryMessageSizeLimit(session.getBinaryMessageSizeLimit*4)
    logger.debug(s"Connection established. With user $id")

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
      Future {
        logger.debug("Creating new actor after 1 second")
        Thread.sleep(1000)
        logger.debug("1 second has been passed")
        actorSystem.actorOf(Props(new MessageActor(session)), id)
      }.onComplete(actRef => {
        //here created actor sends pong message to check it connection
        logger.debug("Created actor " + actRef.get.toString())
        session.sendMessage(new PongMessage())
      })

    })

  }

  override def afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus): Unit = {

    logger.debug("Closing connection has been reached")

    val id = session.getHandshakeHeaders.get("id").get(0)
    //looking for user's actor
    actorSystem.actorSelection(s"/user/${id}").resolveOne(FiniteDuration(1, "s")).onComplete(a => {
      logger.debug("The Future has been reached")
      //if found stopping it
      if (a.isSuccess) {
        logger.debug("Future is success")
        logger.debug("Removing actor")
        actorSystem.stop(a.get)
      }
    })
  }

  override def supportsPartialMessages(): Boolean = true

  private def getId(session: WebSocketSession): String =
    session.getHandshakeHeaders get "id" get 0

  private val logger = LogManager.getLogger(MessageHandler.this)

}
