package com.example.csit228f2_2;

import com.example.csit228f2_2.MySQLConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {
    public static void main(String[] args) {
        try(Connection c = MySQLConnection.getConnection();
            Statement statement = c.createStatement()) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(50) NOT NULL, " +
                    "password VARCHAR(50) NOT NULL)";
            statement.execute(createTableQuery);
            System.out.println("Table Created Successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
