package com.peekaboo.messaging.socket.worker

abstract class Action(val parameters: Map[String, String]) {
  val name: String

  def getParameter(key: String): Option[String] = parameters.get(key)

  def getBody: Array[Byte]
}

case class Send(text: Array[Byte], override val parameters: Map[String, String]) extends Action(parameters) {

  override val name = "SEND"

  override def getBody = text

  def toMessage(author: String): Message = Message(text, parameters.filter { case (param, _) => param != ParameterName.Destination } + (ParameterName.From -> author))

  def getDestination: String = getParameter(ParameterName.Destination).get

  def getType: String = getParameter(ParameterName.Type).get


}

class Header(name: String, parameters: Map[String, String]) {
  def size = parameters.map { case (key, value) => key + ":" + value }.foldLeft(name)((res, cur) => res + "\n" + cur).length
  def toSend(payload: Array[Byte]) = Send(payload, parameters)

}

case class Message(text: Array[Byte], override val parameters: Map[String, String]) extends Action(parameters) {

  override val name = "MESSAGE"

  override def getBody = text
}

class SendText(text: String, parameters: Map[String, String]) extends Send(text.getBytes("UTF-8"), parameters + (ParameterName.Type -> Type.Text))

class SendAudio(text: Array[Byte], parameters: Map[String, String]) extends Send(text, parameters + (ParameterName.Type -> Type.Audio))

object Type {
  val Audio = "audio"
  val Text = "text"
}

object ParameterName {
  val Type = "type"
  val Destination = "destination"
  val From = "from"
  val Date = "date"
  val Reason = "reason"
}