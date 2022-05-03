package com.eep.TFGRestaurant.service;

import com.eep.TFGRestaurant.entity.comandas.ComandasDTO;
import com.eep.TFGRestaurant.entity.comandas.ComandasEntity;
import com.eep.TFGRestaurant.entity.comandas.ComandasResponse;
import com.eep.TFGRestaurant.entity.comandaspagadas.ComandasPagadasEntity;

import java.util.ArrayList;
import java.util.List;

public interface ComandasService {

    public abstract List<ComandasEntity> listAll();
    public abstract boolean add(ComandasEntity comandasEntity);
    public abstract boolean delete(int id);
    public abstract boolean update(ComandasEntity comandasEntity);
    public abstract ComandasEntity findByMesa(int mesa);
    public abstract boolean addProductos(ComandasDTO comandasDTO, ArrayList<String> seleccionados);
    public abstract boolean deleteProductos(ComandasDTO comandasDTO, ArrayList<Integer> seleccionados);


    public abstract ComandasEntity DtoToEntity(ComandasDTO comandasDto);
    public abstract ComandasResponse EntityToResponse(ComandasEntity comandasEntity);
    public abstract ComandasPagadasEntity sinPagarToPagada(ComandasEntity comandasEntity);

    public abstract double total(ComandasEntity comandasEntity);
    public abstract boolean pagar(ComandasEntity comandasEntity);
    public abstract boolean pagada(ComandasEntity comandasEntity);

}
