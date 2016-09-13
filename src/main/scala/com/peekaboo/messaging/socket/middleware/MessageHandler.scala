package com.peekaboo.messaging.socket.middleware

import java.util

import akka.actor.Props
import com.google.gson.Gson
import com.peekaboo.messaging.socket.worker._
import com.peekaboo.model.Neo4jSessionFactory
import com.peekaboo.model.repository.impl.UserRepositoryImpl
import org.apache.logging.log4j.LogManager
import org.springframework.web.socket._
import org.springframework.web.socket.handler.BinaryWebSocketHandler

import scala.collection.mutable
import scala.collection.JavaConversions._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

//Implementation of Spring BinaryWebSocketHandler
//I'm not sure, but the container(tomcat) creates new thread for each request to this endpoint
//So it would be a good idea to replace Spring websockets with something offered by one of the scala's frameworks
class MessageHandler(requestDispatcher: RequestDispatcher, messageInterceptor: MessageInterceptor) extends BinaryWebSocketHandler {
  val userRepository = new UserRepositoryImpl(new Neo4jSessionFactory);
  //each message goes through this method
  override def handleBinaryMessage(session: WebSocketSession, message: BinaryMessage): Unit = {
    try {
      logger.debug("Message size: " + message.getPayloadLength)

      //at first we have to parse incoming message
      val action = messageInterceptor.handle(message.getPayload.array())

      //at second we have to recognize who is an initiator
      val ownerId = getId(session)
      logger.debug("want to process action")
      //finally, we dispatch the message
      requestDispatcher.process(action, ownerId)

    }
    catch {
      case e: IllegalArgumentException =>
        logger.warn("Error. Cannot parse" + new String(message.getPayload.array()))

    }

  }

  private val actorSystem = ActorSystems.messageSystem

  //this method is invoked after successful handshake with client and the sever
  //also it would be great to send this user all messages he has received while he was offline
  override def afterConnectionEstablished(session: WebSocketSession): Unit = {


    val id = getId(session)
    //next line was used when we had been trying to send binary data via sockets
    //session.setBinaryMessageSizeLimit(session.getBinaryMessageSizeLimit * 4) //todo: check does it work
    logger.debug(s"Connection established. With user $id")
    //at first it looks if there was a connection with a client
    try {
      actorSystem.actorOf(Props(new DefaultMessageActor("0")), "0")
    }
    catch{case e:Exception=>}
    actorSystem.actorSelection(s"/user/${id}").resolveOne(FiniteDuration(1, "s")).onComplete(a => {
      logger.debug("Future has been reached")

      //if the connection existed stop actor handling it
      if (a.isSuccess) {
        logger.debug("Future is success")
        logger.debug("Removing actor")
        actorSystem.stop(a.get)
      }

      //after one second create new actor with connection
      //this was done to give enough time for system to stop the actor
      Future {
        //        actorSystem.actorSelection(s"/user/${id}").resolveOne(FiniteDuration(1, "s")).onComplete(a => {
        //          logger.debug("IF actor is alive, kill it")
        //          //if found stopping it
        //          if (a.isSuccess) {
        //            logger.debug("Removing actor")
        //            actorSystem.stop(a.get)
        //          }
        //        })
        logger.debug("Creating new actor after 1 second")
        Thread.sleep(1000)
        logger.debug("1 second has been passed")
        actorSystem.actorOf(Props(new MessageActor(session)), id)
      }.onComplete(actRef => {
        //here created actor sends pong message to check it's connection
        logger.debug("Created actor " + actRef.get.toString())
        session.sendMessage(new PongMessage())
        val actSel = actRef.get
        val map = new mutable.HashMap[String,String]
        try{
            val user = userRepository.findById(id.toLong)
            logger.error("found user")

            val messages = userRepository.getPendingMessagesFor(user)
            logger.error(messages.toString)

            val list: util.ArrayList[Message] = new util.ArrayList[Message]()
            messages.get(id).toStream.foreach{msg => list.add(new Gson().fromJson[Message](msg, Message.getClass))}

            list.foreach{ msg => actSel ! msg}

        }catch{case a:Exception=>logger.error(a.getStackTrace)}


//        map.+=(ParameterName.From,)
//        val message a=new Message()s
//        actSel ! ()
      })

    })

  }

  //this method will be invoked only if client manually invokes "close connection", or server do it
  //this method wont be invoked if client lose connection with internet
  override def afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus): Unit = {

    logger.debug("Closing connection has been reached")

    val id = getId(session)
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