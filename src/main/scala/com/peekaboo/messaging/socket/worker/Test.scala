package com.peekaboo.messaging.socket.worker

import java.io._
import java.util

import akka.actor.{ActorRef, ActorSelection, Props}
import org.apache.logging.log4j.LogManager

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by Oleksii on 10.09.2016.
  */

class ObjectInputStreamWithCustomClassLoader(
                                              inputStream: ByteArrayInputStream
                                            ) extends ObjectInputStream(inputStream) {
  override def resolveClass(desc: java.io.ObjectStreamClass): Class[_] = {
    try { Class.forName(desc.getName, false, getClass.getClassLoader) }
    catch { case ex: ClassNotFoundException => super.resolveClass(desc) }
  }
}

object Test {
  def main(args:Array[String]): Unit ={

    Future{chatsystem.actorOf(Props[ChatManager],"chat")}.onComplete(a=>{
      logger.error("created"+a.toString)
    })
    Thread.sleep(2000)
    try{
    Future{chatsystem.actorOf(Props[ChatManager],"chat")}.onComplete(a=>{
      logger.error("created"+a.toString)
    })}
    catch{case e =>logger.error("was previously created")}
    Thread.sleep(2000)
    Future{chatsystem.actorSelection("/user/chat")}.onComplete(a=>{
      logger.error("got"+a.get.toString())
      logger.error("success"+a.isSuccess)
      val c=a.get
      val i=1
      c!(i)
      Thread.sleep(2000)
      actorRefs+=(c->"ololo")
      logger.error("size:"+actorRefs.size)
    })

  }
var actorRefs = scala.collection.mutable.Map[ActorSelection,String]()
  val logger=LogManager.getLogger(this)
  val chatsystem = akka.actor.ActorSystem.create()
}
