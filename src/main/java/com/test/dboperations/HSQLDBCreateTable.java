package com.test.dboperations;

import com.test.fileprocessing.PropertyFileUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class HSQLDBCreateTable {
    private final Connection connection;
    private static final String createTableSQL = "CREATE TABLE IF NOT EXISTS PUBLIC.SERVERLOGS (\r\n  event_id  varchar(30) primary key,\r\n  event_duration DOUBLE,\r\n  type varchar(30),\r\n  host varchar(30),\r\n  alert varchar(6)\r\n  );";
    public static Logger logger = Logger.getLogger(HSQLDBCreateTable.class.getName());

    public HSQLDBCreateTable(Connection connection) {
        this.connection = connection;
    }

    public void createTable() {
        String log4jConfPath = System.getProperty("user.dir") + PropertyFileUtil.getProperty("log4JProperties");
        PropertyConfigurator.configure(log4jConfPath);

        try {
            Statement statement = this.connection.createStatement();
            statement.execute(createTableSQL);
            logger.debug("Table is created or already exists");
        } catch (SQLException var3) {
            HSQLJDBCUtils.printSQLException(var3);
            logger.error(var3.getMessage());
        } catch (Exception var4) {
            var4.printStackTrace();
            logger.error(var4.getMessage());
        }

    }
}
