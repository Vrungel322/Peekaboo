package com.peekaboo.messaging.socket.middleware

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorRef, Props}
import com.peekaboo.messaging.socket._
import com.peekaboo.messaging.socket.worker._
import org.apache.logging.log4j.LogManager

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.FiniteDuration
import scala.util.Try

class StandardSocketRequestDispatcher extends RequestDispatcher {

  val system = ActorSystems.messageSystem

  def init = {
    system.actorOf(Props(new FileSenderActor), "dispatcher")
  }

  def process(action: Action, authorId: String) = {
    action match {
      case a: Send =>
        val destination = a.getDestination
        logger.debug(s"SEND action from ${authorId} to ${destination}")

        //todo: if not found actor use default
        //upd: doesn't work right now
        val dest = if (a.getType == Type.Audio) authorId else a.getDestination

        system.actorSelection("/user/" + dest).resolveOne(FiniteDuration(1, "s")).onComplete(aTry => {
          val actSel = getFromTry(aTry, dest)

          actSel ! (a, authorId)
        })


    }
  }


  private def getFromTry(aTry: Try[ActorRef], defaultHandler: String): ActorRef = {
    if (aTry.isSuccess) aTry.get
    else system.actorOf(Props(new DefaultMessageActor(defaultHandler)))
  }

  private val logger = LogManager.getLogger(this)

  private class FileSenderActor extends Actor{
    override def receive: Receive = {
      case a: FileAction =>
        system.actorSelection("/user/" + a.id).resolveOne(FiniteDuration(1, "s")).onComplete(aTry => {
          val actSel = getFromTry(aTry, a.id)

          actSel ! a
        })
    }
  }

}
