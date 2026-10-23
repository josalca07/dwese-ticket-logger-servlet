package org.iesalixar.daw2.jorge.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.iesalixar.daw2.jorge.entity.Region;

public class RegionDAOImpl implements RegionDAO {

    @Override
    public List<Region> listAllRegions() throws SQLException {
        List<Region> regions = new ArrayList<>();
        String query = "SELECT * FROM regions";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String code = resultSet.getString("code");
                String name = resultSet.getString("name");
                regions.add(new Region(id, code, name));
            }
        }
        return regions;
    }

    @Override
    public void insertRegion(Region region) throws SQLException {
        String query = "INSERT INTO regions (code, name) VALUES (?, ?)";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, region.getCode());
            preparedStatement.setString(2, region.getName());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void updateRegion(Region region) throws SQLException {
        String query = "UPDATE regions SET code = ?, name = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, region.getCode());
            preparedStatement.setString(2, region.getName());
            preparedStatement.setInt(3, region.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void deleteRegion(int id) throws SQLException {
        String query = "DELETE FROM regions WHERE id = ?";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public Region getRegionById(int id) throws SQLException {
        String query = "SELECT * FROM regions WHERE id = ?";
        Region region = null;
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String code = resultSet.getString("code");
                    String name = resultSet.getString("name");
                    region = new Region(id, code, name);
                }
            }
        }
        return region;
    }

    @Override
    public boolean existsRegionByCode(String code) throws SQLException {
        String sql = "SELECT COUNT(*) FROM regions WHERE UPPER(code) = ?";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, code.toUpperCase());
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        }
    }

    @Override
    public boolean existsRegionByCodeAndNotId(String code, int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM regions WHERE UPPER(code) = ? AND id != ?";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, code.toUpperCase());
            statement.setInt(2, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        }
    }
}