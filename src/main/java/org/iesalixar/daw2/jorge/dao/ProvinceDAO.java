package org.iesalixar.daw2.jorge.dao;

import org.iesalixar.daw2.jorge.entity.Province;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz para gestionar las operaciones CRUD de Provincias en la base de datos.
 */
public interface ProvinceDAO {
    List<Province> listAllProvinces() throws SQLException;
    void insertProvince(Province province) throws SQLException;
    void updateProvince(Province province) throws SQLException;
    void deleteProvince(int id) throws SQLException;
    Province getProvinceById(int id) throws SQLException;
    boolean existsProvinceByCode(String code) throws SQLException;
    boolean existsProvinceByCodeAndNotId(String code, int id) throws SQLException;
}