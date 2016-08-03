package com.peekaboo.messaging.socket.middleware

import akka.actor.{ActorRef, Props}
import com.peekaboo.messaging.socket._
import com.peekaboo.messaging.socket.worker._
import org.apache.logging.log4j.LogManager

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.FiniteDuration
import scala.util.Try

class StandardSocketRequestDispatcher extends RequestDispatcher {

  val system = MessageActorSystem.system

  def process(action: Action, authorId: String) = {
    action match {
      case a: Send =>
        val destination = a.getDestination
        logger.debug(s"SEND action from ${authorId} to ${destination}")

        //todo: if not found actor use default
        //upd: doesn't work right now

        system.actorSelection("/user/" + destination).resolveOne(FiniteDuration(1, "s")).onComplete(aTry => {
          val actSel = getFromTry(aTry, destination)

          actSel ! (a, authorId)
        })


    }
  }

  private def getFromTry(aTry: Try[ActorRef], defaultHandler: String): ActorRef = {
    if (aTry.isSuccess) aTry.get
    else system.actorOf(Props(new DefaultMessageActor(defaultHandler)))
  }

  private val logger = LogManager.getLogger(this)

}
