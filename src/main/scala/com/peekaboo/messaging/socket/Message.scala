package com.peekaboo.messaging.socket

abstract class Message

case class TextMessage(text: String, receiver: String, sender: String) extends Message

case class AudioMessage(location: String, receiver: String, sender: String) extends Message

case class ChangeStatus(status: Int) extends Message
