package com.peekaboo.messaging.socket

import org.apache.logging.log4j.LogManager

abstract class Action(private val parameters: Map[String, String]) {
  val name: String
  def getParameter(key: String): Option[String] = parameters.get(key)
  def getBody: Array[Byte]
}

object Action {
  def compose(action: Action): Array[Byte] = {
    logger.debug(s"Composing action ${action.name}")
    val header = (action.parameters.foldLeft(action.name + "\n")((acc, item) => acc + s"${item._1}:${item._2}\n") + "\n").getBytes("UTF-8")
    val body = action.getBody
    val resultArray = new Array[Byte](header.length + body.length)
    System.arraycopy(header, 0, resultArray, 0, header.length)
    System.arraycopy(body, 0, resultArray, header.length, body.length)
    resultArray
  }


  private val logger = LogManager.getLogger(Action.this)
}

case class Send(text: Array[Byte], map: Map[String, String]) extends Action(map) {

  override val name = "SEND"

  def this(text: Array[Byte], destination: String, `type`: String, parameters: Map[String, String] = Map()) =
    this(text, parameters + ("destination" -> destination, "type" -> `type`))

  override def getBody: Array[Byte] = text

  def getDestination: String = getParameter("destination").get
}

object CopyTest extends App {
  val send = Send("Hello".getBytes("UTF-8"), Map("destination" -> "/user/vladislav"))
  println(new String(Action.compose(send)))
}