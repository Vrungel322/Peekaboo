package com.peekaboo.messaging.socket

import org.apache.logging.log4j.LogManager

object RequestDispatcher {

  val system = MessageActorSystem.system

  def process(action: Action, authorId: String) = {
    action match {
      case a: Send =>
        val destination = a.getDestination
        logger.debug(s"SEND action from ${authorId} to ${destination}")

        //todo: if not found actor use default
        val actSel = system.actorSelection("/user/" + destination)

        actSel ! (a, authorId)
    }
  }

  private val logger = LogManager.getLogger(RequestDispatcher.this)

}
