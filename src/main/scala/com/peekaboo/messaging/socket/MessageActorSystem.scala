package com.peekaboo.messaging.socket

import akka.actor.ActorSystem

object MessageActorSystem {
  val system = ActorSystem("messageActor")
}
