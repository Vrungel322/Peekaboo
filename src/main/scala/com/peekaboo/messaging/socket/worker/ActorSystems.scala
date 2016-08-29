package com.peekaboo.messaging.socket.worker

import akka.actor.ActorSystem

//Probably this is a bad tone of doing thins in scala. Please don't blame me.
object ActorSystems {
  val messageSystem = ActorSystem("messageActor")


}
