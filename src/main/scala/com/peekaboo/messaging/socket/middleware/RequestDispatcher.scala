package com.peekaboo.messaging.socket.middleware

import akka.actor.Actor
import com.peekaboo.messaging.socket.worker.Action

trait RequestDispatcher {

  def process(action: Action, initiatorId: String)

}
