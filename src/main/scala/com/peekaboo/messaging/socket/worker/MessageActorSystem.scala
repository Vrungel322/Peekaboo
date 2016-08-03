package com.peekaboo.messaging.socket.worker

import akka.actor.ActorSystem

object MessageActorSystem {
  val system = ActorSystem("messageActor")

}
