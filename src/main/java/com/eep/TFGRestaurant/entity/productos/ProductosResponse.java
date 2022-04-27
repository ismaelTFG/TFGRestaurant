package com.eep.TFGRestaurant.entity.productos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductosResponse {

    private String id;
    private String nombre;
    private String categoria;
    private double precio;

}
