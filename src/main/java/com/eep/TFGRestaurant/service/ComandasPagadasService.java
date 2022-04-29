package com.eep.TFGRestaurant.service;

import com.eep.TFGRestaurant.entity.comandaspagadas.ComandasPagadasDto;
import com.eep.TFGRestaurant.entity.comandaspagadas.ComandasPagadasEntity;
import com.eep.TFGRestaurant.entity.comandaspagadas.ComandasPagadasResponse;

import java.util.List;

public interface ComandasPagadasService {

    public abstract List<ComandasPagadasEntity> listAll();
    public abstract boolean add(ComandasPagadasEntity comandasPagadasEntity);
    public abstract boolean update(ComandasPagadasEntity comandasPagadasEntity);

    public abstract ComandasPagadasEntity dtoToEntity(ComandasPagadasDto comandasPagadasDto);
    public abstract ComandasPagadasResponse entityToResponse(ComandasPagadasEntity comandasPagadasEntity);

}
