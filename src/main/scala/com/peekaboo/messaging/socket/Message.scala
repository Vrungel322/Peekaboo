package com.peekaboo.messaging.socket

abstract class Message

case class Text(text: String, receiver: String, sender: String) extends Message

case class Audio(location: String, receiver: String, sender: String) extends Message

case class ChangeStatus(status: Int) extends Message
