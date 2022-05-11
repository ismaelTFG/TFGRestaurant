package com.eep.TFGRestaurant.service;

import com.eep.TFGRestaurant.entity.comandaspagadas.ComandasPagadasDto;
import com.eep.TFGRestaurant.entity.comandaspagadas.ComandasPagadasEntity;
import com.eep.TFGRestaurant.entity.comandaspagadas.ComandasPagadasResponse;

import java.util.ArrayList;
import java.util.List;

public interface ComandasPagadasService {

    public abstract List<ComandasPagadasEntity> listAll();
    public abstract boolean add(ComandasPagadasEntity comandasPagadasEntity);
    public abstract boolean update(ComandasPagadasEntity comandasPagadasEntity);
    public abstract boolean delete(String id);
    public abstract ComandasPagadasEntity findById(String id);

    public abstract ComandasPagadasEntity dtoToEntity(ComandasPagadasDto comandasPagadasDto);
    public abstract ComandasPagadasResponse entityToResponse(ComandasPagadasEntity comandasPagadasEntity);
    public abstract List<ComandasPagadasResponse> listEntityToListResponse(List<ComandasPagadasEntity> list);

    public abstract List<ComandasPagadasResponse> filtro(ComandasPagadasDto comandasPagadasDto);
    public abstract List<Integer> mesa(List<ComandasPagadasEntity> list);
    public abstract List<String> camarero(List<ComandasPagadasEntity> list);
    public abstract void manyDelete(ArrayList<String> id);

}
