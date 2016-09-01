package com.peekaboo.messaging.socket.worker

//represents different actions client can perform with the system and
//system can perform with client
abstract class Action(val parameters: Map[String, String]) {
  val name: String

  def getParameter(key: String): Option[String] = parameters.get(key)

  def getBody: Array[Byte]
}


//Send command from client to server
case class Send(text: Array[Byte], override val parameters: Map[String, String]) extends Action(parameters) {

  override val name = "SEND"

  override def getBody = text

  /**
    * Converts SEND command from client1 to MESSAGE command to client2
    * @param author author of the SEND command
    * @return MESSAGE command
    */
  def toMessage(author: String): Message = Message(text, parameters.filter { case (param, _) => param != ParameterName.Destination } + (ParameterName.From -> author))

  def getDestination: String = getParameter(ParameterName.Destination).get

  def getType: String = getParameter(ParameterName.Type).get

}

case class Switchmode(text: Array[Byte], override val parameters: Map[String, String]) extends Action(parameters) {

  override val name = "SWITCHMODE"

  override def getBody = text

  /**
    * Converts SEND command from client1 to MESSAGE command to client2
    * @param author author of the SEND command
    * @return MESSAGE command
    */
  def toMessage(author: String): Message = Message(text, parameters.filter { case (param, _) => param != ParameterName.Destination } + (ParameterName.From -> author))

  def getDestination: String = getParameter(ParameterName.Destination).get

  def getType: String = getParameter(ParameterName.Type).get

}

case class ReadNotification(text: Array[Byte], override val parameters: Map[String, String]) extends Action(parameters) {

  override val name = "READNOTIFICATION"

  override def getBody = text

  /**
    * Converts SEND command from client1 to MESSAGE command to client2
    * @param author author of the SEND command
    * @return MESSAGE command
    */
  def toMessage(author: String): SystemMessage = SystemMessage(text, parameters.filter { case (param, _) => param != ParameterName.Destination } + (ParameterName.From -> author))

  def getDestination: String = getParameter(ParameterName.Destination).get

  def getType: String = getParameter(ParameterName.Type).get

}
//Message command from server to client
case class Message(text: Array[Byte], override val parameters: Map[String, String]) extends Action(parameters) {

  override val name = "MESSAGE"

  override def getBody = text
}

case class SystemMessage(text: Array[Byte], override val parameters: Map[String, String]) extends Action(parameters) {

  override val name = "READ_NOTIFICATION"

  override def getBody = text
}

//Probably will be needed in future, so I will leave it here
class SendText(text: String, parameters: Map[String, String]) extends Send(text.getBytes("UTF-8"), parameters + (ParameterName.Type -> Type.Text))

class SendAudio(text: Array[Byte], parameters: Map[String, String]) extends Send(text, parameters + (ParameterName.Type -> Type.Audio))


//Different constants
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