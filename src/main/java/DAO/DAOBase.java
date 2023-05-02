package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DAOBase {
    public Connection getConnection(){
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/oopCA6";
        String username = "root";
        String password = "";
        Connection connection = null;

        try{
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        }
        catch(ClassNotFoundException e){
            System.out.println("Encountered ClassNotFound Exception ");
            e.printStackTrace();
            System.exit(1);
        }
        catch(SQLException e){
            System.out.println("getConnection threw SQLException");
            System.exit(2);
        }

        return connection;
    }

    public void closeConnection(Connection connection){
        try{
            if(connection != null){
                connection.close();
                connection = null;
            }
        }
        catch(SQLException e){
            System.out.println("SQLException when closing connection");
        }
    }
}
