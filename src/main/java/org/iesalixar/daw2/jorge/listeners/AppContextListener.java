package org.iesalixar.daw2.jorge.listeners;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.iesalixar.daw2.jorge.dao.DatabaseConnectionManager;
import org.iesalixar.daw2.jorge.dao.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.io.InputStream;

@WebListener
public class AppContextListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(AppContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Inicializando la aplicación y conectando a la base de datos...");
        try (Connection connection = DatabaseConnectionManager.getConnection()) {
            InputStream sqlFileStream = sce.getServletContext().getResourceAsStream("/WEB-INF/classes/data.sql");
            if (sqlFileStream == null) {
                logger.error("No se pudo encontrar el archivo data.sql en /WEB-INF/classes/");
                return;
            }
            DataInitializer.loadDataFromSQL(sqlFileStream);
            logger.info("Carga de datos finalizada.");
        } catch (Exception e) {
            logger.error("Error al inicializar la aplicación y cargar los datos: {}", e.getMessage(), e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Cerrando la conexión a la base de datos al apagar la aplicación...");
        DatabaseConnectionManager.closeConnection();
    }
}