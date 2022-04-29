package com.eep.TFGRestaurant.entity.comandaspagadas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComandasPagadasResponse {

    private String id;
    private int mesa;
    private String camarero;
    private String fecha;
    private List<String> productos = new ArrayList<>();
    private double total;

}
