package com.peekaboo.messaging.socket

import org.apache.logging.log4j.LogManager

object MessageInterceptor {

  /**
    * Handles received message.
    * Expects message in such format:
    * <pre>
    * COMMAND
    * key1:value1
    * key2:value2
    * key3:value3
    *
    * &lt;body&gt;
    * </pre>
    * It parses Command name and parameters and leaves body unchanged.
    * Otherwise throws Error
    *
    * @param bytes - message to be parsed
    * @return paresed <code>Action</code> with all parameters and a body
    */
  def handle(bytes: Array[Byte]): Action = {
    logger.debug("Parsing command name")
    val (commandName, remain) = getLine(bytes.toList)
    logger.debug(s"Command is ${commandName}")
    logger.debug("Parsing parameters")
    val (parameters, body) = getParameters(remain)
    commandName match {
      case "SEND" => Send(body.toArray, parameters)
      case _ =>
        logger.debug("Command is unknown. Throwing an error")
        throw new Error("Unknown command name")
    }
  }

  private val NewLine = "\n".getBytes("UTF-8")(0)
  private val CarriageReturn = "\r".getBytes("UTF-8")(0)

  /**
    * Parses bytes array until finds new line symbol.
    * Returns line and remainder from initial array
    *
    * @param bytes - bytes to be parsed
    * @return line and remainder
    */
  private def getLine(bytes: List[Byte]): (String, List[Byte]) = {
    //    val (lineBytes, remainder) = bytes.span(_ == newLine)
    //    (new String(lineBytes.toArray), remainder)
    def findLine(line: List[Byte], bytes: List[Byte]): (List[Byte], List[Byte]) = {
      bytes match {
        case Nil => (line, Nil)
        case NewLine :: CarriageReturn :: rest => (line, rest)
        case NewLine :: rest => (line, rest)
        case byte :: rest => findLine(byte :: line, rest)
      }
    }
    val (lineByte, remain) = findLine(List(), bytes)
    (new String(lineByte.reverse.toArray), remain)
  }

  /**
    * Parses bytes array and gets all parameters from it.
    * Returns map with parameters and remain bytes list
    *
    * @param bytes - bytes to be parsed
    * @return map with parameters and remain bytes list
    */
  def getParameters(bytes: List[Byte]): (Map[String, String], List[Byte]) = {
    def internalParameterParser(bytes: List[Byte], map: Map[String, String]): (Map[String, String], List[Byte]) = {
      //gets current line and remain
      val (line, remain) = getLine(bytes)

      //if current line is empty then we've reached end of the parameters -> return result
      if (line.isEmpty) (map, remain)
      else {
        //parse line to find key-value pair
        val key :: value :: Nil = line.split(":").toList
        internalParameterParser(remain, map + (key -> value))
      }
    }

    internalParameterParser(bytes, Map())
  }

  private val logger = LogManager.getLogger(MessageInterceptor.this)
}

object ParserTest extends App {
  val str =
      "SEND\n\r" +
      "destination:/user/vladislav\n\r" +
      "\n" +
      "Hello, vlad"
  val act = MessageInterceptor.handle(str.getBytes("UTF-8"))
  println(act.name)
  println(act.getParameter("destination"))
  println(new String(act.getBody))

}
