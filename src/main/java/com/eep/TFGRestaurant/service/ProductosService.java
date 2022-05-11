package com.eep.TFGRestaurant.service;

import com.eep.TFGRestaurant.entity.productos.ProductosDto;
import com.eep.TFGRestaurant.entity.productos.ProductosEntity;
import com.eep.TFGRestaurant.entity.productos.ProductosResponse;

import java.util.ArrayList;
import java.util.List;

public interface ProductosService {

    public abstract List<ProductosEntity> listAll();
    public abstract boolean add(ProductosEntity productos);
    public abstract boolean delete(String id);
    public abstract boolean update(ProductosEntity productos);
    public abstract ProductosEntity findById(String id);

    public abstract ProductosEntity DtoToEntity(ProductosDto productos);
    public abstract ProductosResponse ResponseToEntity(ProductosEntity productos);
    public abstract List<ProductosResponse> listResponseToListEntity(List<ProductosEntity> list);

    public abstract boolean productoNoRepetido(ProductosEntity productos);
    public abstract List<ProductosEntity> busquedacategoria(String categoria);
    public abstract boolean manyDelete(ArrayList<String> id);

}
