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

    //conexion con la base de datos
    @Autowired
    @Qualifier("fireBase")
    private FireBase fireBase;

    /**
     * lista de todos los productos
     * @return lista
     */
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

    /**
     * anadir producto
     * @param productos producto
     * @return si se guarda o no
     */
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

    /**
     * eliminar producto
     * @param id del producto a eliminar
     * @return si se elimina o no
     */
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

    /**
     * modificacion producto
     * @param productos a modificar
     * @return si se modifica o no
     */
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

    /**
     * busqueda por id
     * @param id del producto
     * @return producto
     */
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

    /**
     * casteo de dto a entity
     * @param productos produto dto
     * @return producto entity
     */
    @Override
    public ProductosEntity DtoToEntity(ProductosDto productos) {

        return new ProductosEntity(productos.getId(), productos.getNombre(), productos.getCategoria(), productos.getPrecio());

    }

    /**
     * casteo de entity a response
     * @param productos producto entity
     * @return producto response
     */
    @Override
    public ProductosResponse ResponseToEntity(ProductosEntity productos) {

        return new ProductosResponse(productos.getId(), productos.getNombre(), productos.getCategoria(), productos.getPrecio());

    }

    /**
     * casteo de lista entity a lista response
     * @param list entity
     * @return lista response
     */
    @Override
    public List<ProductosResponse> listResponseToListEntity(List<ProductosEntity> list) {

        List<ProductosResponse> exit = new ArrayList<>();

        for (ProductosEntity i:list){

            exit.add(ResponseToEntity(i));

        }

        return exit;

    }

    /**
     * metodo que comprueba que el producto no exista ya
     * @param productos producto
     * @return si existe o no
     */
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

    /**
     * busqueda de los productos por la categoria
     * @param categoria filtro
     * @return lista de productos
     */
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

    /**
     * eliminar vario productos
     * @param id de los productos
     * @return si se elimina o no
     */
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
