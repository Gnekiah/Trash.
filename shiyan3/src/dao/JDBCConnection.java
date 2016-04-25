package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnection {
    private Connection connection = null;
    public JDBCConnection() {
        // TODO Auto-generated constructor stub
    }
    
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;"+"DatabaseName=album","sa","142576");
        return connection;
    }
}
