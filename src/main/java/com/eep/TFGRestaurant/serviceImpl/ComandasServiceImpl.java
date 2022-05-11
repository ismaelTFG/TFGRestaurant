package com.eep.TFGRestaurant.serviceImpl;

import com.eep.TFGRestaurant.entity.comandas.ComandasEntity;
import com.eep.TFGRestaurant.entity.comandas.ComandasDTO;
import com.eep.TFGRestaurant.entity.comandas.ComandasResponse;
import com.eep.TFGRestaurant.entity.comandaspagadas.ComandasPagadasEntity;
import com.eep.TFGRestaurant.entity.productos.ProductosEntity;
import com.eep.TFGRestaurant.entity.productos.ProductosResponse;
import com.eep.TFGRestaurant.repository.FireBase;
import com.eep.TFGRestaurant.service.ComandasPagadasService;
import com.eep.TFGRestaurant.service.ComandasService;
import com.eep.TFGRestaurant.service.ProductosService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service("comandasServiceImpl")
public class ComandasServiceImpl implements ComandasService {

    @Autowired
    @Qualifier("fireBase")
    private FireBase fireBase;

    @Autowired
    @Qualifier("productosServiceImpl")
    private ProductosService productosService;

    @Autowired
    @Qualifier("comandasPagadasServiceImpl")
    private ComandasPagadasService comandasPagadasService;

    @Override
    public List<ComandasEntity> listAll() {

        List<ComandasEntity> list = new ArrayList<>();
        ComandasEntity comandas;

        CollectionReference comand = fireBase.getFirestore().collection("comandassinpagar");
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = comand.get();

        try {

            for (DocumentSnapshot i: querySnapshotApiFuture.get().getDocuments()) {

                comandas = i.toObject(ComandasEntity.class);
                comandas.setMesa(Integer.parseInt(i.getId()));

                list.add(comandas);

            }

        } catch (InterruptedException | ExecutionException e) {

            e.printStackTrace();

        }

        return list;

    }

    @Override
    public boolean add(ComandasEntity comandasEntity) {

        Map<String, Object> docData = new HashMap<>();

        docData.put("camarero", comandasEntity.getCamarero());
        docData.put("fecha", LocalDate.now().toString());

        CollectionReference users = fireBase.getFirestore().collection("comandassinpagar");
        ApiFuture<WriteResult> writeResultApiFuture = users.document(comandasEntity.getMesa()+"").create(docData);

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
    public boolean delete(int id) {

        CollectionReference users = fireBase.getFirestore().collection("comandassinpagar");
        ApiFuture<WriteResult> writeResultApiFuture = users.document(id+"").delete();

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
    public boolean update(ComandasEntity comandasEntity) {

        ComandasEntity id = findByMesa(comandasEntity.getMesa());

        if (comandasEntity.getCamarero().equals("")){

            comandasEntity.setCamarero(id.getCamarero());

        }

        Map<String, Object> docData = new HashMap<>();

        docData.put("camarero", comandasEntity.getCamarero());
        docData.put("fecha", LocalDate.now().toString());
        docData.put("productos", comandasEntity.getProductos());

        CollectionReference users = fireBase.getFirestore().collection("comandassinpagar");
        ApiFuture<WriteResult> writeResultApiFuture = users.document(comandasEntity.getMesa()+"").set(docData);

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
    public ComandasEntity findByMesa(int mesa) {

        List<ComandasEntity> list = listAll();

        for (ComandasEntity i:list){
            if (i.getMesa() == mesa){

                return i;

            }
        }

        return null;
    }

    @Override
    public boolean addProductos(ComandasDTO comandasDTO, ArrayList<String> seleccionados) {

        List<ProductosEntity> list = productosService.listAll();
        ComandasEntity comandasEntity = findByMesa(comandasDTO.getMesa());
        List<String> add = comandasEntity.getProductos();

        for (String i:seleccionados){
            for (ProductosEntity j:list){
                if (j.getId().equals(i)){

                    add.add(j.getId());

                }
            }
        }

        comandasDTO.setProductos(add);

        update(DtoToEntity(comandasDTO));

        return false;

    }

    @Override
    public boolean deleteProductos(ComandasDTO comandasDTO, ArrayList<Integer> seleccionados) {

        ComandasEntity comandasEntity = findByMesa(comandasDTO.getMesa());
        List<String> add = comandasEntity.getProductos();

        for (Integer i:seleccionados){

            int posicion = i;

            add.set(posicion, "");

        }

        int largo = (add.size()) - 1;

        for (int i = largo; i >= 0; i--){
            if (add.get(i).equals("")){

                add.remove(i);

            }
        }

        comandasEntity.setProductos(add);
        update(comandasEntity);

        return false;
    }

    @Override
    public ComandasEntity DtoToEntity(ComandasDTO comandasDto) {

        return new ComandasEntity(comandasDto.getMesa(), comandasDto.getCamarero(), comandasDto.getFecha(), comandasDto.getProductos());

    }

    @Override
    public ComandasResponse EntityToResponse(ComandasEntity comandasEntity) {

        return new ComandasResponse(comandasEntity.getMesa(), comandasEntity.getCamarero(), comandasEntity.getFecha(), comandasEntity.getProductos());

    }

    @Override
    public List<ComandasResponse> listEntityToListResponse(List<ComandasEntity> list) {

        List<ComandasResponse> exit = new ArrayList<>();

        for (ComandasEntity i:list){

            exit.add(EntityToResponse(i));

        }

        return exit;

    }

    @Override
    public ComandasPagadasEntity sinPagarToPagada(ComandasEntity comandasEntity) {

        return new ComandasPagadasEntity(comandasEntity.getMesa(), comandasEntity.getCamarero(), comandasEntity.getFecha(), comandasEntity.getProductos(), total(comandasEntity));

    }

    @Override
    public boolean mesaNoRepetida(ComandasEntity comandasEntity) {

        List<ComandasEntity> list = listAll();

        for (ComandasEntity i:list){
            if (i.getMesa() == comandasEntity.getMesa()){

                return true;

            }
        }

        return false;

    }

    @Override
    public List<ProductosResponse> productos(ComandasEntity comandasEntity) {

        List<ProductosResponse> exit = new ArrayList<>();
        List<ProductosEntity> list = productosService.listAll();

        for (String j:comandasEntity.getProductos()){
            for (ProductosEntity i:list){
                if (i.getId().equals(j)){

                    exit.add(productosService.ResponseToEntity(i));

                }
            }
        }

        return exit;

    }

    @Override
    public List<Integer> mesa(List<ComandasEntity> list) {

        List<Integer> exit = new ArrayList<>();

        for (ComandasEntity i:list){

            exit.add(i.getMesa());

        }

        return exit;

    }

    @Override
    public List<String> camarero(List<ComandasEntity> list) {

        List<String> exit = new ArrayList<>();

        for (int i = 0; i < list.size(); i++){

            boolean norepeti = true;

            if (i == 0){

                exit.add(list.get(i).getCamarero());

            }else {

                int largo = exit.size();

                for (int j = 0; j < largo; j++){
                    if (exit.get(j).equals(list.get(i).getCamarero())){

                        norepeti = false;

                    }
                }

                if (norepeti){

                    exit.add(list.get(i).getCamarero());

                }

            }

        }

        return exit;

    }

    @Override
    public List<ComandasResponse> filtro(ComandasEntity comandasEntity) {

        List<ComandasResponse> exit = new ArrayList<>();
        List<ComandasEntity> list = listAll();

        if (comandasEntity.getMesa() == 0){
            if (!comandasEntity.getCamarero().equals("*")){

                for (ComandasEntity i:list){
                    if (i.getCamarero().equals(comandasEntity.getCamarero())){

                        exit.add(EntityToResponse(i));

                    }
                }

            }
        }else if (comandasEntity.getCamarero().equals("*")){

            for (ComandasEntity i:list){
                if (i.getMesa() == comandasEntity.getMesa()){

                    exit.add(EntityToResponse(i));

                }
            }

        }else {

            for (ComandasEntity i:list){
                if (i.getCamarero().equals(comandasEntity.getCamarero())){
                    if (i.getMesa() == comandasEntity.getMesa()){

                        exit.add(EntityToResponse(i));

                    }
                }
            }

        }

        return exit;

    }

    @Override
    public void manyDelete(ArrayList<Integer> mesa) {

        for (int i:mesa){

            delete(i);

        }

    }

    @Override
    public double total(ComandasEntity comandasEntity) {

        double exit = 0;
        List<ProductosEntity> list = productosService.listAll();

        for (String i : comandasEntity.getProductos()){
            for (ProductosEntity j : list){
                if (j.getNombre().equals(i)){

                    exit = exit + j.getPrecio();

                }
            }
        }

        return exit;

    }

    @Override
    public boolean pagar(ComandasEntity comandasEntity) {

        File cabezal = new File("src\\main\\resources\\txt\\cabezalticket.txt");
        File ticket = new File("src\\main\\resources\\txt\\ticket.txt");
        BufferedReader reader;
        BufferedWriter writer;
        String line;
        String text = "";

        try {

            reader = new BufferedReader(new FileReader(cabezal));

            while ((line = reader.readLine()) != null){

                text = text+line+"\n";

            }

            text = text+"\n"+comandasEntity.toString(productosService.listAll())+"\n TOTAL "+total(comandasEntity)+"€";

            writer = new BufferedWriter(new FileWriter(ticket));

            writer.write(text);

            reader.close();
            writer.close();

            return true;

        } catch (IOException e) {

            e.printStackTrace();

        }

        return false;
    }

    @Override
    public boolean pagada(ComandasEntity comandasEntity) {

        comandasPagadasService.add(sinPagarToPagada(comandasEntity));
        delete(comandasEntity.getMesa());

        return false;

    }

}
