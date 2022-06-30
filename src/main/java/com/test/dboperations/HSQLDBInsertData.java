package com.test.dboperations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class HSQLDBInsertData {
    public static Connection connection;
    public static HSQLJDBCUtils utils;
    public static HSQLDBCreateTable createTable;
    public static Logger logger = Logger.getLogger(HSQLDBInsertData.class.getName());
    private static final String INSERT_LOG_SQL = "INSERT INTO PUBLIC.SERVERLOGS  (event_id, event_duration, type, host, alert) VALUES  (?, ?, ?, ?, ?);";

    public HSQLDBInsertData() {
    }

    public void insertRecord(Map<String, String> insertDataMap) throws ClassNotFoundException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_LOG_SQL);
            preparedStatement.setString(1, insertDataMap.get("id"));
            preparedStatement.setString(2, insertDataMap.get("duration"));
            preparedStatement.setString(3, insertDataMap.get("type"));
            preparedStatement.setString(4, insertDataMap.get("host"));
            preparedStatement.setString(5, insertDataMap.get("alert"));
            logger.debug("Insert SQL: " + preparedStatement);
            preparedStatement.executeUpdate();
            logger.debug("Log Event Record is Inserted to database for event id: " + insertDataMap.get("id"));
        } catch (SQLIntegrityConstraintViolationException var3) {
            logger.debug("Log Event Record is exists in database for event id: " + insertDataMap.get("id"));
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    static {
        String log4jConfPath = System.getProperty("user.dir") + "//PropertyFiles//log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);
        utils = new HSQLJDBCUtils();
        connection = utils.getConnection();
        createTable = new HSQLDBCreateTable(connection);
        createTable.createTable();
    }
}
