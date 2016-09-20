package com.peekaboo.miscellaneous;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Oleksii on 20.09.2016.
 */
public  class JavaPropertiesParser {
    String propertiesPath = "src\\main\\resources\\config\\application.properties";
    Logger logger = LogManager.getLogger(this);
    public static final JavaPropertiesParser PARSER=new JavaPropertiesParser();
    public  String getValue(String Key){
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Properties prop = new Properties();
            InputStream input=JavaPropertiesParser.class.getResourceAsStream("/config/application.properties");
            prop.load(input);

            return prop.getProperty(Key);
        } catch (Exception e){
                logger.error("Error with reading file application.properties " + e.toString());
                return null;
        }
    }
}
