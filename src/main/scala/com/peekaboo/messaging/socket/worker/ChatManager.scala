package com.peekaboo.messaging.socket.worker

import akka.actor.{Actor, ActorRef}
import org.apache.logging.log4j.LogManager


class ChatManager extends Actor {

  private var actorRefs = scala.collection.mutable.Map[ActorRef, String]()


  override def receive = {
    case (msg: Message, sender: String, destination: String, messageType: String) => {
      logger.error("got to chat")
      logger.error("number of users in chat is:" + actorRefs.size)
      for ((ref, id) <- actorRefs) {
        logger.error("sending message to :" + id)
        if (id != sender) {
          ref ! (msg, destination, id, messageType)
        }
      }
    }
    case (msg: SystemMessage, sender: String, destination: String, messageType: String) => {
      logger.error("got to chat")
      logger.error("number of users in chat is:" + actorRefs.size)
      for ((ref, id) <- actorRefs) {
        logger.error("sending message to :" + id)
        if (id != sender) {
          ref ! (msg, destination, id, messageType)
        }
      }
    }

    case (a: ActorRef, id: String, command: String) => {
      logger.error("got to chatmanager")
      if (command == "delete") {
        logger.error("deleted actor from chat:" + a.toString())
        actorRefs -= (a);
      }
      if (command == "add") {
        logger.error("added actor to chat:" + a.toString())
        actorRefs += (a -> id);
      }
    }
    //    case (_)=>{
    //      logger.error("received:" )
    //    }
  }

  def setActorRefs(buffer: scala.collection.mutable.Map[ActorRef, String]): Unit = {
    actorRefs = buffer;
  }

  def getActorRefs: scala.collection.mutable.Map[ActorRef, String] = {
    actorRefs
  }

  def addActorRef(ref: ActorRef, id: String): Unit = {
    actorRefs += (ref -> id);
  }

  private val logger = LogManager.getLogger(this)
}
