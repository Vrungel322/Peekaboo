package com.peekaboo.messaging.socket.middleware

import akka.actor.{Actor, ActorRef, Props}
import com.peekaboo.messaging.socket.worker.{SystemMessage, _}
import com.peekaboo.model.Neo4jSessionFactory
import com.peekaboo.model.entity.enums.UserState
import com.peekaboo.model.repository.impl.UserRepositoryImpl
import com.peekaboo.model.service.impl.UserServiceImpl
import org.apache.logging.log4j.LogManager

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.FiniteDuration
import scala.util.Try

class StandardSocketRequestDispatcher extends RequestDispatcher {
  val userRepository = new UserRepositoryImpl(new Neo4jSessionFactory);
  val system = ActorSystems.messageSystem

  def process(action: Action, authorId: String) = {
    logger.debug("Got to process")
    action match {
      case a: Message =>
        val destination = a.getDestination
        val messageType=a.getType
        logger.error("HERE IS SOMETHING")
        logger.error("messageType:"+messageType)

        logger.debug(s"SEND action from ${authorId} to ${destination}")



        //todo: if not found actor use default
        system.actorSelection("/user/" + destination).resolveOne(FiniteDuration(1, "s")).onComplete(aTry => {
          val actSel = getFromTry(aTry, destination)

          actSel ! (a, authorId,destination,messageType)
        }
        )


      //      case a:Switchmode=>
      //
      //        user.setState(a.getBody(0).toInt)
      case a:SystemMessage=>
        if (a.getReason=="mode"){
          logger.error("got to REASON:MODE")
          logger.debug(new String(a.getBody))
          logger.debug(a.getBody(0))
          val user=userRepository.findById(authorId.toLong)
          logger.debug(user)
          user.setState(a.getBody(0).toInt)
          userRepository.update(user)
        } else{
          val destination = a.getDestination
          val messageReason=a.getReason
          logger.error("System message is processed")
          logger.error("messageReason"+messageReason)
          logger.debug(s"System action from ${authorId} to ${destination}")



          //todo: if not found actor use default
          system.actorSelection("/user/" + destination).resolveOne(FiniteDuration(1, "s")).onComplete(aTry => {
            val actSel = getFromTry(aTry, destination)
            logger.error("Trying to get to actor")
            actSel ! (a, authorId,destination,messageReason)
          })}

      case a=> logger.error("unknown command")
    }
  }

  /**
    * Tries to get an actor from the Try.
    * If there is no actor in the Try it means that client isn't connected to system in this moment
    * should return default actor, which responsibility is saving messages to the database
    *
    * -----------------------------------------------
    * For some unknown for me reasons it doesn't work
    * Good luck fixing it
    * -----------------------------------------------
    *
    * @param aTry Try from which actor is going to be fetched
    * @param defaultHandler name of the defaultHandler for current user
    * @return actor, which is able to receive Actions
    */
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

  private val logger = LogManager.getLogger(this)

}