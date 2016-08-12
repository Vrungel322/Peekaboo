package com.peekaboo.messaging.socket.worker

import akka.actor.ActorSystem

object ActorSystems {
  val messageSystem = ActorSystem("messageActor")
  val fileSystem = ActorSystem("fileActor")

}
