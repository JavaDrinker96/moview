package com.example.moview.config.db;

import com.example.moview.exception.DataBaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {

    static final Logger log = (Logger) LogManager.getRootLogger();
    static private boolean driverDownloaded = false;
    static private final String JDBC_URL = "jdbc:postgresql://localhost:5432/moview";
    static private final String DB_USER = "postgres";
    static private final String DB_PASSWORD = "postgres";

    public static Connection getConnection() {
        if (!driverDownloaded) {
            loadDriver();
        }

        try {
            log.info("Getting a connection to the database.");
            final Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
            log.info("Getting a connection to the database was successful.");
            return connection;
        } catch (SQLException e) {
            log.error("Error getting a connection to the database.");
            throw new DataBaseException("Error getting a connection to the database.");
        }
    }

    private static void loadDriver() {
        log.info("Loading the database driver.");
        try {
            Class.forName("org.postgresql.Driver");
            log.info("Loading of the database driver was successful.");
            driverDownloaded = true;
        } catch (ClassNotFoundException e) {
            log.error("Error loading the database driver.");
            throw new DataBaseException("Error loading the database driver.");
        }
    }
}
