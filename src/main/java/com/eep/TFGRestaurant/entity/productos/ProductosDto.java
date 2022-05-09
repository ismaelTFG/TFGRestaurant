package com.eep.TFGRestaurant.entity.productos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductosDto {

    @NotBlank(message = "El codigo no cumple los requisitos necesarios.")
    private String id;
    @NotBlank(message = "El nombre no cumple los requisitos necesarios.")
    private String nombre;
    private String categoria;
    @Min(value = 0, message = "el precio no puede ser inferior a 0€")
    private double precio;

}
