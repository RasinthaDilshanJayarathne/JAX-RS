/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.api.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rasintha_j
 */
public class DatabaseConfig {
    
//    private static DatabaseConfig dbConnection = null;
//    private Connection connection;
//
//    private DatabaseConfig() throws ClassNotFoundException, SQLException {
//        Class.forName("com.mysql.cj.jdbc.Driver");
//        connection = DriverManager.getConnection(
//                "jdbc:mysql://localhost:3306/health?sessionVariables=sql_mode='NO_ENGINE_SUBSTITUTION'&jdbcCompliantTruncation=false",
//                "root",
//                ""
//        );
//    }
//    public static DatabaseConfig getInstance() throws SQLException, ClassNotFoundException {
//        if (dbConnection==null){
//            dbConnection = new DatabaseConfig();
//        }
//        return dbConnection;
//    }
//    public Connection getConnection(){
//        return connection;
//    }
    
    private static DatabaseConfig config;
    private Connection con;

    private DatabaseConfig() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/healthapi?sessionVariables=sql_mode='NO_ENGINE_SUBSTITUTION'&jdbcCompliantTruncation=false",
                    "root", "");
            con.setAutoCommit(false);
            System.out.println("Database connection established successfully.");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Failed to establish database connection:");
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseConfig getInstance() {
        if (config == null) {
            config = new DatabaseConfig();
        }
        return config;
    }

    public Connection getConnection() {
        return con;
    }

    public void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error occurred while closing database connection:");
            e.printStackTrace();
        }
    }

}
