package com.peekaboo.messaging.socket

import akka.util.Timeout
import org.apache.logging.log4j.LogManager

object RequestDispatcher {

  val system = MessageActorSystem.system

  def process(action: Action, authorId: String) = {
    logger.debug("Got message from " + authorId)
    action match {
      case a: Send =>
        val destination = a.getDestination
        logger.debug(s"Looking for the actor with id ${destination}")
//        val optionalAction = ActorPool.findActor(a.getDestination)
//        logger.debug(s"Actor isEmpty=${optionalAction.isEmpty}")
//        val actor = optionalAction.get
        val actSel = system.actorSelection("/user/" + destination)
        logger.debug("Actor selection: " + actSel)
        actSel ! (a, authorId)
    }
  }

  private val logger = LogManager.getLogger(RequestDispatcher.this)

}
