package com.peekaboo.messaging.socket.worker

import java.io._
import java.util

import scala.collection.mutable

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
//    val message: Message = new Message("fuck".getBytes,
//      new Map[String, String]() +("dfvb" -> "sdfg"))
////  val message: Message = new Message()
//    val aos = new ByteArrayOutputStream()
//    val oos = new ObjectOutputStream(aos)
//    oos.writeObject(message)
//    oos.close
//      println(aos.toString)
//
//
//    val ais=new ByteArrayInputStream(aos.toByteArray)
//    val b=new ObjectInputStreamWithCustomClassLoader(ais);
//////    var b=Class.forName("com.peekaboo.messaging.socket.worker.Message",false,getClass.getClassLoader)
//    val x = b.readObject
//    b.close
  }

}
