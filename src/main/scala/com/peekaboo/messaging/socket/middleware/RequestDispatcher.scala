package com.peekaboo.messaging.socket.middleware

import com.peekaboo.messaging.socket.worker.Action

trait RequestDispatcher {

  def process(action: Action, initiatorId: String)

}
