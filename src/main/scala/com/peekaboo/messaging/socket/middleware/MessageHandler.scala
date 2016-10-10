package com.peekaboo.messaging.socket.middleware

import java.util

import akka.actor.{ActorRef, Props}

import scala.collection.JavaConverters._
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.peekaboo.messaging.socket.worker._
import com.peekaboo.model.Neo4jSessionFactory
import com.peekaboo.model.repository.impl.UserRepositoryImpl
import org.apache.logging.log4j.LogManager
import org.springframework.context.ApplicationListener
import org.springframework.web.socket._
import org.springframework.web.socket.handler.BinaryWebSocketHandler
import org.springframework.web.socket.messaging.SessionDisconnectEvent

import scala.collection.mutable
import scala.collection.JavaConversions._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration
import upickle.default._

import scala.util.Try
//Implementation of Spring BinaryWebSocketHandler
//I'm not sure, but the container(tomcat) creates new thread for each request to this endpoint
//So it would be a good idea to replace Spring websockets with something offered by one of the scala's frameworks
class StompConnectEvent extends ApplicationListener[SessionDisconnectEvent] {

  private val logger = LogManager.getLogger(this)
override def onApplicationEvent( event:SessionDisconnectEvent)= {
logger.error("Disconnected!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
}}
class MessageHandler(requestDispatcher: RequestDispatcher, messageInterceptor: MessageInterceptor) extends BinaryWebSocketHandler {
  val userRepository = new UserRepositoryImpl(new Neo4jSessionFactory)
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
      logger.debug("Got to process")
      action match {
        case a: Message =>
          val destination = a.getDestination
          val messageType=a.getType
          logger.error("HERE IS SOMETHING")
          logger.error("messageType:"+messageType)

          logger.debug(s"SEND action from ${ownerId} to ${destination}")


          if (destination.toLong==294){
            //MEGOFITCHA______________________________________________________________________
            logger.error("CHAT MESSAGE")
            try{
              Future{chatsystem.actorSelection("/user/chat")}.onComplete(actorref=>{
                val c=actorref.get
                logger.error("got message actor:"+a.toString)
                c!(a, ownerId, destination, messageType)
              })
            }
            catch{case e =>logger.error("was previously created")}
            //MEGOFITCHA_____________________________________________________________________

          }
          else {
            //todo: if not found actor use default
            system.actorSelection("/user/" + destination).resolveOne(FiniteDuration(1, "s")).onComplete(aTry => {
              val actSel = getFromTry(aTry, destination)

              actSel !(a, ownerId, destination, messageType)
            }
            )
          }

        //      case a:Switchmode=>
        //
        //        user.setState(a.getBody(0).toInt)
        case a:SystemMessage=>

          if (a.getReason=="mode"){
            logger.error("got to REASON:MODE")
            logger.debug(new String(a.getBody))
            logger.debug(a.getBody(0))
            val user=userRepository.findById(ownerId.toLong)
            logger.debug(user)
            user.setState(a.getBody(0).toInt)
            userRepository.update(user)
          } else {
            val destination = a.getDestination
            val messageReason = a.getReason
            logger.error("System message is processed")
            logger.error("messageReason" + messageReason)
            logger.debug(s"System action from ${ownerId} to ${destination}")

            if (destination.toLong == 294) {
              //MEGOFITCHA______________________________________________________________________
              logger.error("CHAT MESSAGE")
              try {
                Future {
                  chatsystem.actorSelection("/user/chat")
                }.onComplete(actorref => {
                  val c = actorref.get
                  logger.error("got message actor:" + a.toString)
                  c !(a, ownerId, destination, messageReason)
                })
              }
              catch {
                case e => logger.error("was previously created")
              }
              //MEGOFITCHA_____________________________________________________________________
            }
            else {
              //todo: if not found actor use default
              system.actorSelection("/user/" + destination).resolveOne(FiniteDuration(1, "s")).onComplete(aTry => {
                val actSel = getFromTry(aTry, destination)
                logger.error("Trying to get to actor")
                actSel !(a, ownerId, destination, messageReason)
              })
            }
          }
        case a=> logger.error("unknown command")
      }

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
   //MEGOFITCHA______________________________________________________________________
    try{
      Future{chatsystem.actorOf(Props[ChatManager],"chat")}.onComplete(a=>{
        logger.error("created"+a.toString)
      })}
    catch{case e =>logger.error("was previously created")}
    //MEGOFITCHA_____________________________________________________________________



    val id = getId(session)
    //next line was used when we had been trying to send binary data via sockets
    //session.setBinaryMessageSizeLimit(session.getBinaryMessageSizeLimit * 4) //todo: check does it work
    logger.debug(s"Connection established. With user $id")
    //at first it looks if there was a connection with a client

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


          //MEGOFITCHA______________________________________________________________________
          try{
            Future{chatsystem.actorSelection("/user/chat")}.onComplete(a=>{
              val c=a.get
              logger.error("adding actor to chat")
              c!(actSel,id,"add")
              Thread.sleep(2000)
            })
          }
          catch{case e =>logger.error("was previously created")}
          //MEGOFITCHA_____________________________________________________________________

          val map = new mutable.HashMap[String,String]
          try{
            val user = userRepository.findById(id.toLong)

            logger.error("got to block try and user is"+user)
            val messages: util.Map[String, util.List[String]] = userRepository.getPendingMessagesFor(user)
            logger.debug("Number of messages:" + messages.size())

            val list: util.ArrayList[Message] = new util.ArrayList[Message]()
            for((a,v)<-messages){
              for(z<-v){
                logger.error(z)
                val msg = read[Message](z)
                logger.error(msg)
                actSel ! (msg,msg.getSender,id,msg.getType)
              }
            }
            userRepository.deletePendingMessages(user)

          }catch{case a:Exception=>logger.error(a.toString)}


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

      //MEGOFITCHA______________________________________________________________________
      try{
        Future{chatsystem.actorSelection("/user/chat")}.onComplete(ac=>{
          val c=ac.get
          c!(a.get,id,"delete")
          Thread.sleep(2000)
        })
      }
      catch{case e =>logger.error("was previously created")}
      //MEGOFITCHA_____________________________________________________________________

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

  private def getFromTry(aTry: Try[ActorRef], defaultHandler: String): ActorRef = {
    if (aTry.isSuccess) {
      logger.error("Atry is success")
      aTry.get
    }
    else {
      logger.error("default handler")
      system.actorOf(Props(new DefaultMessageActor(defaultHandler)), defaultHandler)
    }
  }

  val system = ActorSystems.messageSystem
  val chatsystem=ActorSystems.chatSystem
  private val logger = LogManager.getLogger(MessageHandler.this)

}