package com.peekaboo.messaging.socket.worker

import akka.actor.Actor
import org.apache.logging.log4j.LogManager


//Dummy implementation
//If user isn't connected to the system (not online), and he gets a message from someone
//We have to store his message somewhere, for this purpose DefaultMessageActor is going to be used
class DefaultMessageActor(private val id: String) extends Actor {


  override def receive: Receive = {
    case a: Send =>
      logger.debug(s"User ${id} is offline now. Let's pretend that I can save his message in database.")
      logger.debug(s"The message was ${new String(a.getBody)}")
  }

  private val logger = LogManager.getLogger(DefaultMessageActor.this)
}
