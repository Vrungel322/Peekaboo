package com.peekaboo.messaging.socket.middleware

import com.peekaboo.messaging.socket.worker.Action

/**
  * This trait is used for intercepting messages from sockets.
  *
  */
trait MessageInterceptor {
  def handle(bytes: Array[Byte]): Action



  //  def compose(action: Action): AnyRef
}
