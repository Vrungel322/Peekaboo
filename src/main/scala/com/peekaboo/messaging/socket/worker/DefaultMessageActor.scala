package com.peekaboo.messaging.socket.worker

import akka.actor.Actor
import com.google.gson.Gson
import com.peekaboo.model.Neo4jSessionFactory
import com.peekaboo.model.entity.User
import com.peekaboo.model.repository.impl.UserRepositoryImpl
import org.apache.logging.log4j.LogManager
//Dummy implementation
//If user isn't connected to the system (not online), and he gets a message from someone
//We have to store his message somewhere, for this purpose DefaultMessageActor is going to be used
class DefaultMessageActor(private val id: String) extends Actor {
  val userService = new UserRepositoryImpl(new Neo4jSessionFactory)
  private val logger = LogManager.getLogger(DefaultMessageActor.this)

  override def receive: Receive = {
    case (a: Message, receiver: String,destination:String,messageType:String) =>
      logger.debug(s"User ${receiver} is offline now. Let's pretend that I can save his message in database.")
      logger.debug(s"The message was ${new String(a.getBody)}")
      val userFrom: User = userService.findById(receiver.toLong)
      val userTo: User = userService.findById(destination.toLong)
      userService.addPendingMessage(userFrom, userTo,a.getType, new Gson().toJson(a.getBody));
  }
}
