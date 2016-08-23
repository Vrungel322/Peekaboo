package com.peekaboo.messaging.socket.middleware

import akka.actor.{Actor, ActorRef, Props}
import com.peekaboo.messaging.socket.worker._
import org.apache.logging.log4j.LogManager

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.FiniteDuration
import scala.util.Try

class StandardSocketRequestDispatcher extends RequestDispatcher {
  val system = ActorSystems.messageSystem

  def process(action: Action, authorId: String) = {
    action match {
      case a: Send =>
        try{
          val destinationId = a.getDestination
        this.logger.debug(s"SEND action from $authorId to $destinationId")



        //todo: if not found actor use default
        //todo: there is a big bullshit in the code- actor doesnot save
        system.actorSelection("/user/" + destinationId).resolveOne(FiniteDuration(1, "s")).onComplete(aTry => {
          val actSel = getFromTry(aTry, destinationId)

          actSel ! (a,destinationId)

        })
        }catch{case ex:Exception =>
          logger.error(ex.getMessage)
        }


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
    if (aTry.isSuccess) aTry.get
    else system.actorOf(Props(new DefaultMessageActor(defaultHandler)), defaultHandler)
  }

  private val logger = LogManager.getLogger(this)

}
