package com.peekaboo.messaging.file

import java.io.{File, FileOutputStream}

import akka.actor.{Actor, ActorRef}
import com.peekaboo.messaging.socket.worker.{FileAction, ActorSystems}
import org.apache.logging.log4j.LogManager

class FileActor(val id: String, val fileName: String, val author: String) extends Actor{

  val fold = new File(id)
  if (!fold.exists()) fold.mkdir()
  val file = new File(fold, fileName)
  file.createNewFile()

  val fileStream = new FileOutputStream(file)

  override def receive: Receive = {
    case piece: Array[Byte] =>
      logger.debug("Got piece. Writing it to file")
      fileStream.write(piece)
  }


  @scala.throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
      logger.debug("Got close action in actor. Closing file. And sending ")
      fileStream.close()
      ActorSystems.messageSystem.actorSelection("/user/dispatcher") ! FileAction(file.getName, id, author)
  }

  private val logger = LogManager.getLogger(this)
}

