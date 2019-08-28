package Connectivity;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionClass {

    public Connection connection;

    public Connection getConnection(){
        String dbName = "journal_app";
        String userName = "root";
        String password = "";
        String driverName = "com.mysql.jdbc.Driver";
        try{
            Class.forName(driverName);
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+dbName,userName,password);
        }catch (Exception e){
            e.printStackTrace();
        }

        return connection;
    }

}
