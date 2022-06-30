
package com.test.dboperations;

import com.test.fileprocessing.PropertyFileUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class HSQLJDBCUtils {
    public Connection connection;
    public static Logger log = Logger.getLogger(HSQLJDBCUtils.class.getName());
    private final String jdbcURL = PropertyFileUtil.getProperty("jdbcURL");
    private final String jdbcUsername = PropertyFileUtil.getProperty("jdbcUsername");
    private final String jdbcPassword = PropertyFileUtil.getProperty("jdbcPassword");

    public HSQLJDBCUtils() {
    }

    public Connection getConnection() {
        try {
            assert this.jdbcURL != null;
            this.connection = DriverManager.getConnection(this.jdbcURL, this.jdbcUsername, this.jdbcPassword);
            log.debug("Connected to HSQL Database");
        } catch (SQLException var2) {
            var2.printStackTrace();
        }

        return this.connection;
    }

    public static void printSQLException(SQLException ex) {
        Iterator<Throwable> var1 = ex.iterator();

        while(true) {
            Throwable e;
            do {
                if (!var1.hasNext()) {
                    return;
                }

                e = var1.next();
            } while(!(e instanceof SQLException));

            e.printStackTrace(System.err);
            System.err.println("SQLState: " + ((SQLException)e).getSQLState());
            System.err.println("Error Code: " + ((SQLException)e).getErrorCode());
            System.err.println("Message: " + e.getMessage());

            for(Throwable t = ex.getCause(); t != null; t = t.getCause()) {
                System.out.println("Cause: " + t);
            }
        }
    }

    static {
        String log4jConfPath = System.getProperty("user.dir") + PropertyFileUtil.getProperty("log4JProperties");
        PropertyConfigurator.configure(log4jConfPath);

        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        } catch (ClassNotFoundException var2) {
            var2.printStackTrace();
        }

    }
}
