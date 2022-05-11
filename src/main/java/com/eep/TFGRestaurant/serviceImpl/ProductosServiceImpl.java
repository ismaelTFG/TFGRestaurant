package com.eep.TFGRestaurant.serviceImpl;

import com.eep.TFGRestaurant.entity.productos.ProductosDto;
import com.eep.TFGRestaurant.entity.productos.ProductosEntity;
import com.eep.TFGRestaurant.entity.productos.ProductosResponse;
import com.eep.TFGRestaurant.repository.FireBase;
import com.eep.TFGRestaurant.service.ProductosService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service("productosServiceImpl")
public class ProductosServiceImpl implements ProductosService {

    @Autowired
    @Qualifier("fireBase")
    private FireBase fireBase;

    @Override
    public List<ProductosEntity> listAll() {

        List<ProductosEntity> list = new ArrayList<>();
        ProductosEntity productos;

        CollectionReference produc = fireBase.getFirestore().collection("productos");
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = produc.get();

        try {

            for (DocumentSnapshot i: querySnapshotApiFuture.get().getDocuments()) {

                productos = i.toObject(ProductosEntity.class);
                productos.setId(i.getId());

                list.add(productos);

            }

        } catch (InterruptedException | ExecutionException e) {

            e.printStackTrace();

        }

        return list;

    }

    @Override
    public boolean add(ProductosEntity productos) {

        Map<String, Object> docData = new HashMap<>();

        docData.put("nombre", productos.getNombre());
        docData.put("categoria", productos.getCategoria());
        docData.put("precio", productos.getPrecio());

        CollectionReference users = fireBase.getFirestore().collection("productos");
        ApiFuture<WriteResult> writeResultApiFuture = users.document(productos.getId()).create(docData);

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
    public boolean delete(String id) {

        CollectionReference users = fireBase.getFirestore().collection("productos");
        ApiFuture<WriteResult> writeResultApiFuture = users.document(id).delete();

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
    public boolean update(ProductosEntity productos) {

        Map<String, Object> docData = new HashMap<>();

        docData.put("nombre", productos.getNombre());
        docData.put("categoria", productos.getCategoria());
        docData.put("precio", productos.getPrecio());

        CollectionReference users = fireBase.getFirestore().collection("productos");
        ApiFuture<WriteResult> writeResultApiFuture = users.document(productos.getId()).set(docData);

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
    public ProductosEntity findById(String id) {

        List<ProductosEntity> list = listAll();

        for (ProductosEntity i:list){
            if (i.getId().equals(id)){

                return i;

            }
        }

        return null;

    }

    @Override
    public ProductosEntity DtoToEntity(ProductosDto productos) {

        return new ProductosEntity(productos.getId(), productos.getNombre(), productos.getCategoria(), productos.getPrecio());

    }

    @Override
    public ProductosResponse ResponseToEntity(ProductosEntity productos) {

        return new ProductosResponse(productos.getId(), productos.getNombre(), productos.getCategoria(), productos.getPrecio());

    }

    @Override
    public List<ProductosResponse> listResponseToListEntity(List<ProductosEntity> list) {

        List<ProductosResponse> exit = new ArrayList<>();

        for (ProductosEntity i:list){

            exit.add(ResponseToEntity(i));

        }

        return exit;

    }

    @Override
    public boolean productoNoRepetido(ProductosEntity productos) {

        List<ProductosEntity> list = listAll();

        for (ProductosEntity i:list){
            if (i.getId().equals(productos.getId())){

                return true;

            }
        }

        return false;

    }

    @Override
    public List<ProductosEntity> busquedacategoria(String categoria) {

        List<ProductosEntity> list = listAll();
        List<ProductosEntity> exit = new ArrayList<>();

        for (ProductosEntity i:list){
            if (i.getCategoria().equals(categoria)){

                exit.add(i);

            }
        }

        return exit;

    }

    @Override
    public boolean manyDelete(ArrayList<String> id) {

        List<ProductosEntity> list = listAll();

        if (list.size() > id.size()){

            for (String i:id){

                delete(i);

            }

            return true;

        }

        return false;

    }

}
