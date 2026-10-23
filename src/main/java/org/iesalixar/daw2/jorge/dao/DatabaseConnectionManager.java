package org.iesalixar.daw2.jorge.dao;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConnectionManager {
    private static Connection connection = null;
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionManager.class);
    private static Dotenv dotenv = Dotenv.load();

    private DatabaseConnectionManager() {}

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                logger.info("Iniciando la conexión a la base de datos MariaDB...");
                String dbUrl = dotenv.get("DB_URL");
                String dbUser = dotenv.get("DB_USER");
                String dbPassword = dotenv.get("DB_PASSWORD");
                connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                logger.info("Conexión a la base de datos establecida exitosamente.");
            }
        } catch (SQLException e) {
            logger.error("Error al conectar con la base de datos: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo conectar a la base de datos.", e);
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                logger.info("Cerrando la conexión a la base de datos...");
                connection.close();
                logger.info("Conexión cerrada correctamente.");
            } catch (SQLException e) {
                logger.error("Error al cerrar la conexión a la base de datos: {}", e.getMessage(), e);
            }
        }
    }
}