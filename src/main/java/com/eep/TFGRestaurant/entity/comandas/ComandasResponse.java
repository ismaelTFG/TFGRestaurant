package com.eep.TFGRestaurant.entity.comandas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComandasResponse {

    private int mesa;
    private String camarero;
    private String fecha;
    private List<String> productos = new ArrayList<>();

}
