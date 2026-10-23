package org.iesalixar.daw2.jorge.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa una Provincia en la base de datos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Province {
    private int id;
    private String code;
    private String name;
    private Region region; // Relación con la tabla regions (id_region)

    /**
     * Constructor para crear una provincia antes de insertarla (sin id).
     * @param code Código de la provincia.
     * @param name Nombre de la provincia.
     * @param region Objeto Region al que pertenece.
     */
    public Province(String code, String name, Region region) {
        this.code = code;
        this.name = name;
        this.region = region;
    }
}