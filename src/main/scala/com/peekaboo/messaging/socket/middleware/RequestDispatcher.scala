package com.peekaboo.messaging.socket.middleware

import com.peekaboo.messaging.socket.worker.Action

trait RequestDispatcher {

  /**
    * Processes action to a specific actor
    * @param action to be processed
    * @param initiatorId author of the action
    */
  def process(action: Action, initiatorId: String)

}
