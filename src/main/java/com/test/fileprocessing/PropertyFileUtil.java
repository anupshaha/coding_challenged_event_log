package com.test.fileprocessing;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class PropertyFileUtil {
    public static Properties properties;
    public static FileReader file = null;
    public static String PROPERTYFILEPATH = System.getProperty("user.dir") + "//PropertyFiles//Property.properties";
    static Logger logger = Logger.getLogger(PropertyFileUtil.class.getName());

    public PropertyFileUtil() {
    }

    public static String getProperty(String propertyName) {
        try {
            properties = new Properties();
            file = new FileReader(PROPERTYFILEPATH);
            properties.load(file);
            logger.debug("Property Name:: " + propertyName + " exists in Properties File:: " + PROPERTYFILEPATH);
            return properties.getProperty(propertyName);
        } catch (IOException var2) {
            logger.error("Property Name:: " + propertyName + " not exists in Properties File:: " + PROPERTYFILEPATH);
            var2.printStackTrace();
            return null;
        }
    }

    static {
        String log4jConfPath = System.getProperty("user.dir") + getProperty("log4JProperties");
        PropertyConfigurator.configure(log4jConfPath);
    }
}
