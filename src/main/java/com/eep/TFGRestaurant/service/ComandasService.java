package com.eep.TFGRestaurant.service;

import com.eep.TFGRestaurant.entity.comandas.ComandasDTO;
import com.eep.TFGRestaurant.entity.comandas.ComandasEntity;
import com.eep.TFGRestaurant.entity.comandas.ComandasResponse;
import com.eep.TFGRestaurant.entity.comandaspagadas.ComandasPagadasEntity;
import com.eep.TFGRestaurant.entity.productos.ProductosResponse;

import java.util.ArrayList;
import java.util.List;

public interface ComandasService {

    //metodos que conecta con la base de datos
    public abstract List<ComandasEntity> listAll();
    public abstract boolean add(ComandasEntity comandasEntity);
    public abstract boolean delete(int id);
    public abstract boolean update(ComandasEntity comandasEntity);
    public abstract ComandasEntity findByMesa(int mesa);
    public abstract boolean addProductos(ComandasDTO comandasDTO, ArrayList<String> seleccionados);
    public abstract boolean deleteProductos(ComandasDTO comandasDTO, ArrayList<Integer> seleccionados);

    //metodos de casteo
    public abstract ComandasEntity DtoToEntity(ComandasDTO comandasDto);
    public abstract ComandasResponse EntityToResponse(ComandasEntity comandasEntity);
    public abstract List<ComandasResponse> listEntityToListResponse(List<ComandasEntity> list);
    public abstract ComandasPagadasEntity sinPagarToPagada(ComandasEntity comandasEntity);

    //metodos de logica extra
    public abstract boolean mesaNoRepetida(ComandasEntity comandasEntity);
    public abstract List<ProductosResponse> productos(ComandasEntity comandasEntity);
    public abstract List<Integer> mesa(List<ComandasEntity> list);
    public abstract List<String> camarero(List<ComandasEntity> list);
    public abstract List<ComandasResponse> filtro(ComandasEntity comandasEntity);
    public abstract void manyDelete(ArrayList<Integer> mesa);
    public abstract double total(ComandasEntity comandasEntity);
    public abstract boolean pagar(ComandasEntity comandasEntity);
    public abstract boolean pagada(ComandasEntity comandasEntity);

}
