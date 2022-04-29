package com.eep.TFGRestaurant.entity.comandas;

import com.eep.TFGRestaurant.entity.productos.ProductosEntity;
import com.eep.TFGRestaurant.entity.productos.ProductosResponse;
import com.eep.TFGRestaurant.serviceImpl.ProductosServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComandasEntity {

    private int mesa;
    private String camarero;
    private String fecha;
    private List<String> productos = new ArrayList<>();

    public String toString(List<ProductosEntity> list) {

        StringBuilder sb = new StringBuilder();

        sb.append("-------------------------------------------------\n");
        sb.append("Mesa: ").append(mesa).append(" Camarero: ").append(camarero).append(" Fecha: ").append(fecha).append("\n");
        sb.append("-------------------------------------------------\n");
        sb.append("unidad, ").append("descripcion, ").append("precio, ").append("importe\n");
        sb.append("-------------------------------------------------\n");

        for (String i : productos) {
            for (ProductosEntity j : list) {
                if (i.equals(j.getId())) {
                    sb.append(1).append(", ").append(j.getNombre()).append(", ").append(j.getPrecio()).append(", ").append(j.getPrecio()).append("\n");
                }
            }
        }

        sb.append("-------------------------------------------------\n");

        return sb.toString();

    }
}
