package com.example.moview.moview.config.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSourceJdbc {

    private static final Logger log = (Logger) LogManager.getRootLogger();

    private static final String DRIVER_CLASS_NAME = "org.postgresql.Driver";
    private static boolean driverDownloaded = false;
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/moview";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        if (!driverDownloaded) {
            loadDriver();
        }

        log.info(String.format("Getting a connection to the database with url = %s.", JDBC_URL));
        return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
    }

    private static void loadDriver() throws ClassNotFoundException {
        log.info(String.format("Loading the database driver %s.", DRIVER_CLASS_NAME));
        Class.forName(DRIVER_CLASS_NAME);
        driverDownloaded = true;
    }
}
