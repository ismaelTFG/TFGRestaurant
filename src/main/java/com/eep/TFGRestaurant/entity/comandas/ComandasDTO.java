package com.eep.TFGRestaurant.entity.comandas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComandasDTO {

    @Min(value = 0, message = "la mesa no puede ser inferior a 0.")
    private int mesa;
    @NotBlank(message = "El camarero no puede estar en blanco")
    private String camarero;
    private String fecha;
    private List<String> productos = new ArrayList<>();

}
