package com.peekaboo.messaging.socket.worker


sealed trait Incoming{

}
case class SystemMsg(text:Array[Byte],val parameters: Map[String, String]) extends Incoming{
  val name = "SYSTEMMESSAGE"
  def getBody = new String(text,"UTF-8")
  def getParameter(key: String): Option[String] = parameters.get(key)
  def getDestination: String = {
    try{getParameter(ParameterName.Destination).get}
    catch{case e:Exception=>new String("nobody")}
  }

  def getReason: String = getParameter(ParameterName.Reason).get
}
case class TextMsg(text:Array[Byte],val parameters: Map[String, String]) extends Incoming{
  val name = "MESSAGE"
  def getBody = new String(text,"UTF-8")
  def getParameter(key: String): Option[String] = parameters.get(key)
  def getDestination: String = {
    try{getParameter(ParameterName.Destination).get}
    catch{case e:Exception=>new String("nobody")}
  }

  def getReason: String = getParameter(ParameterName.Reason).get
}
case class AudioMsg(val parameters: Map[String, String]) extends Incoming
case class VideoMsg() extends Incoming
case class AudioStreamMsg() extends Incoming
case class VideoStreamMsg() extends Incoming
