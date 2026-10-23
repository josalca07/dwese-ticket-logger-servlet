package org.iesalixar.daw2.jorge.dao;

import org.iesalixar.daw2.jorge.entity.Province;
import org.iesalixar.daw2.jorge.entity.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de ProvinceDAO para interactuar con MariaDB.
 */
public class ProvinceDAOImpl implements ProvinceDAO {
    private static final Logger logger = LoggerFactory.getLogger(ProvinceDAOImpl.class);

    @Override
    public List<Province> listAllProvinces() throws SQLException {
        logger.info("Entrando en listAllProvinces");
        List<Province> provinces = new ArrayList<>();
        String query = "SELECT p.id, p.code, p.name, r.id as r_id, r.code as r_code, r.name as r_name " +
                "FROM provinces p JOIN regions r ON p.id_region = r.id";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Region region = new Region(resultSet.getInt("r_id"), resultSet.getString("r_code"), resultSet.getString("r_name"));
                provinces.add(new Province(resultSet.getInt("id"), resultSet.getString("code"), resultSet.getString("name"), region));
            }
        }
        logger.info("Saliendo de listAllProvinces. Se encontraron {} provincias.", provinces.size());
        return provinces;
    }

    @Override
    public void insertProvince(Province province) throws SQLException {
        logger.info("Entrando en insertProvince para el código {}", province.getCode());
        String query = "INSERT INTO provinces (code, name, id_region) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, province.getCode());
            preparedStatement.setString(2, province.getName());
            preparedStatement.setInt(3, province.getRegion().getId());
            preparedStatement.executeUpdate();
        }
        logger.info("Saliendo de insertProvince");
    }

    @Override
    public void updateProvince(Province province) throws SQLException {
        logger.info("Entrando en updateProvince para el id {}", province.getId());
        String query = "UPDATE provinces SET code = ?, name = ?, id_region = ? WHERE id = ?";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, province.getCode());
            preparedStatement.setString(2, province.getName());
            preparedStatement.setInt(3, province.getRegion().getId());
            preparedStatement.setInt(4, province.getId());
            preparedStatement.executeUpdate();
        }
        logger.info("Saliendo de updateProvince");
    }

    @Override
    public void deleteProvince(int id) throws SQLException {
        logger.info("Entrando en deleteProvince para el id {}", id);
        String query = "DELETE FROM provinces WHERE id = ?";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
        logger.info("Saliendo de deleteProvince");
    }

    @Override
    public Province getProvinceById(int id) throws SQLException {
        logger.info("Entrando en getProvinceById para id {}", id);
        String query = "SELECT p.id, p.code, p.name, r.id as r_id, r.code as r_code, r.name as r_name " +
                "FROM provinces p JOIN regions r ON p.id_region = r.id WHERE p.id = ?";
        Province province = null;

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Region region = new Region(resultSet.getInt("r_id"), resultSet.getString("r_code"), resultSet.getString("r_name"));
                    province = new Province(id, resultSet.getString("code"), resultSet.getString("name"), region);
                }
            }
        }
        logger.info("Saliendo de getProvinceById");
        return province;
    }

    @Override
    public boolean existsProvinceByCode(String code) throws SQLException {
        logger.info("Verificando si existe la provincia con código {}", code);
        String query = "SELECT COUNT(*) FROM provinces WHERE UPPER(code) = ?";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, code.toUpperCase());
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        }
    }

    @Override
    public boolean existsProvinceByCodeAndNotId(String code, int id) throws SQLException {
        logger.info("Verificando si existe la provincia con código {} ignorando el id {}", code, id);
        String query = "SELECT COUNT(*) FROM provinces WHERE UPPER(code) = ? AND id != ?";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, code.toUpperCase());
            statement.setInt(2, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        }
    }
}