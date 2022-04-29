package com.eep.TFGRestaurant.entity.comandaspagadas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComandasPagadasEntity {

    private String id;
    private int mesa;
    private String camarero;
    private String fecha;
    private List<String> productos = new ArrayList<>();
    private double total;

    public ComandasPagadasEntity(int mesa, String camarero, String fecha, List<String> productos, double total){

        this.mesa = mesa;
        this.camarero = camarero;
        this.fecha = fecha;
        this.productos = productos;
        this.total = total;

    }

}
