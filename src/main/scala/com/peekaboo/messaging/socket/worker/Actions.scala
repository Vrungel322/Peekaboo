package com.peekaboo.messaging.socket.worker

//represents different actions client can perform with the system and
//system can perform with client
abstract class Action(val parameters: Map[String, String]) {
  val name: String

  def getParameter(key: String): Option[String] = parameters.get(key)

  def getBody: Array[Byte]
}


//Send command client <--- server
case class SystemMessage(text: Array[Byte], override val parameters: Map[String, String]) extends Action(parameters) {

  override val name = "SYSTEMMESSAGE"
  override def getBody = text

  /**
    * Converts SEND command from client1 to MESSAGE command to client2
    *
    * @param author author of the SEND command
    * @return MESSAGE command
    */
  def toMessage(author: String): SystemMessage = SystemMessage(text, parameters.filter { case (param, _) => param != ParameterName.Destination } + (ParameterName.From -> author))

  def getDestination: String = {
    try{getParameter(ParameterName.Destination).get}
    catch{case e:Exception=>new String("nobody")}
  }

  def getReason: String = getParameter(ParameterName.Reason).get

}
@SerialVersionUID(123L)
case class Message(text: Array[Byte], override val parameters: Map[String, String]) extends Action(parameters) with Serializable {

  override val name = "MESSAGE"

  override def getBody = text

  /**
    * Converts READNOTIFICATION command from client1 to READNOTIFICATION command to client2
    *
    * @param author author of the READNOTIFICATION command
    * @return READ_NOTIFICATION command
    */
  def toMessage(author: String): Message = Message(text, parameters.filter { case (param, _) => param != ParameterName.Destination } + (ParameterName.From -> author))

  def getDestination: String = getParameter(ParameterName.Destination).get

  def getType: String = getParameter(ParameterName.Type).get
}

//Switching command, which client sends to server (switch state - text/audio/video)
//case class Switchmode(text: Array[Byte], override val parameters: Map[String, String]) extends Action(parameters) {
//
//  override val name = "SWITCHMODE"
//
//  override def getBody = text
//
//  /**
//    * SerJust retrieves SWITCHMODE STATUS
//    */
//
//}
////Notification, which client send, when he have read the message, and which client is receiving, when
////his friend have read the message
//}
////Message command server<--client
//case class Message(text: Array[Byte], override val parameters: Map[String, String]) extends Action(parameters) {
//
//  override val name = "MESSAGE"
//
//  override def getBody = text
//
//}
////system message command server--> client
//case class SystemMessage(text: Array[Byte], override val parameters: Map[String, String]) extends Action(parameters) {
//
//  override val name = "SYSTEM"
//
//  override def getBody = text
//}
//
////Probably will be needed in future, so I will leave it here
class SendText(text: String, parameters: Map[String, String]) extends Message(text.getBytes("UTF-8"), parameters + (ParameterName.Type -> Type.Text))

class SendAudio(text: Array[Byte], parameters: Map[String, String]) extends Message(text, parameters + (ParameterName.Type -> Type.Audio))


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

object Reason {
  val Read = "read"
  val Mode = "mode"
}