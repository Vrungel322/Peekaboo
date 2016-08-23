package com.peekaboo.miscellaneous

import java.io.{FileInputStream, InputStream}
import java.util.Properties

import org.apache.logging.log4j.LogManager
/**
  * Created by Glowacki on 16.08.2016.
  */
object PropertiesParser {
  /**
    * Get value by key from .properties files
    * 
    *  WatsonKey Search Key
    * propertiesPath Have default value
    * @return Value by Search Key
    */

  val propertiesPath: String = "src\\main\\resources\\config\\application.properties"
  val logger = LogManager.getLogger(this)
  def getValue(Key: String): String = {
    try {
      val classLoader = Thread.currentThread().getContextClassLoader()
      val input = classLoader.getResourceAsStream("/config/application.properties")
      val prop  = new Properties()
      prop.load (input)
      prop.getProperty(Key)
    }catch { case ex : Exception =>
      logger.error("Error with reading file application.properties "+ex.getMessage)
      null
    }

  }

}
