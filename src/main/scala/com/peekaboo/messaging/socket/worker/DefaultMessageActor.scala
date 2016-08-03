package com.peekaboo.messaging.socket.worker

import akka.actor.Actor
import org.apache.logging.log4j.LogManager

class DefaultMessageActor(private val id: String) extends Actor {


  override def receive: Receive = {
    case a: Send =>
      logger.debug(s"User ${id} is offline now. Let's pretend that I can save his message in database.")
      logger.debug(s"The message was ${new String(a.getBody)}")
  }

  private val logger = LogManager.getLogger(DefaultMessageActor.this)
}
