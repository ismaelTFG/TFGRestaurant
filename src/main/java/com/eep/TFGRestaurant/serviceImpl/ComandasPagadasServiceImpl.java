package com.eep.TFGRestaurant.serviceImpl;

import com.eep.TFGRestaurant.entity.comandaspagadas.ComandasPagadasDto;
import com.eep.TFGRestaurant.entity.comandaspagadas.ComandasPagadasEntity;
import com.eep.TFGRestaurant.entity.comandaspagadas.ComandasPagadasResponse;
import com.eep.TFGRestaurant.repository.FireBase;
import com.eep.TFGRestaurant.service.ComandasPagadasService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service("comandasPagadasServiceImpl")
public class ComandasPagadasServiceImpl implements ComandasPagadasService {

    @Autowired
    @Qualifier("fireBase")
    private FireBase fireBase;

    @Override
    public List<ComandasPagadasEntity> listAll() {
        return null;
    }

    @Override
    public boolean add(ComandasPagadasEntity comandasPagadasEntity) {

        Map<String, Object> docData = new HashMap<>();

        docData.put("mesa", comandasPagadasEntity.getMesa());
        docData.put("camarero", comandasPagadasEntity.getCamarero());
        docData.put("fecha", comandasPagadasEntity.getFecha());
        docData.put("productos", comandasPagadasEntity.getProductos());
        docData.put("total", comandasPagadasEntity.getTotal());

        CollectionReference users = fireBase.getFirestore().collection("comandaspagadas");
        ApiFuture<WriteResult> writeResultApiFuture = users.document().create(docData);

        try {

            if (null != writeResultApiFuture.get()){

                return true;

            }

        } catch (InterruptedException | ExecutionException e) {

            e.printStackTrace();

        }

        return false;

    }

    @Override
    public boolean update(ComandasPagadasEntity comandasPagadasEntity) {
        return false;
    }

    @Override
    public ComandasPagadasEntity dtoToEntity(ComandasPagadasDto comandasPagadasDto) {
        return null;
    }

    @Override
    public ComandasPagadasResponse entityToResponse(ComandasPagadasEntity comandasPagadasEntity) {
        return null;
    }

}
