package com.eep.TFGRestaurant.serviceImpl;

import com.eep.TFGRestaurant.entity.comandas.ComandasEntity;
import com.eep.TFGRestaurant.entity.comandaspagadas.ComandasPagadasDto;
import com.eep.TFGRestaurant.entity.comandaspagadas.ComandasPagadasEntity;
import com.eep.TFGRestaurant.entity.comandaspagadas.ComandasPagadasResponse;
import com.eep.TFGRestaurant.repository.FireBase;
import com.eep.TFGRestaurant.service.ComandasPagadasService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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

        List<ComandasPagadasEntity> list = new ArrayList<>();
        ComandasPagadasEntity add;

        CollectionReference comand = fireBase.getFirestore().collection("comandaspagadas");
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = comand.get();

        try{

            for (DocumentSnapshot i:querySnapshotApiFuture.get().getDocuments()){

                add = i.toObject(ComandasPagadasEntity.class);
                add.setId(i.getId());

                list.add(add);

            }

        } catch (ExecutionException | InterruptedException e) {

            e.printStackTrace();

        }

        return list;

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

        Map<String, Object> docData = new HashMap<>();

        docData.put("mesa", comandasPagadasEntity.getMesa());
        docData.put("camarero", comandasPagadasEntity.getCamarero());
        docData.put("fecha", LocalDate.now().toString());
        docData.put("productos", comandasPagadasEntity.getProductos());
        docData.put("total", comandasPagadasEntity.getTotal());

        CollectionReference pagada = fireBase.getFirestore().collection("comandaspagadas");
        ApiFuture<WriteResult> writeResultApiFuture = pagada.document(comandasPagadasEntity.getId()).set(docData);

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

        CollectionReference pagada = fireBase.getFirestore().collection("comandaspagadas");
        ApiFuture<WriteResult> writeResultApiFuture = pagada.document(id).delete();

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
    public ComandasPagadasEntity findById(String id) {

        List<ComandasPagadasEntity> list = listAll();

        for (ComandasPagadasEntity i:list){
            if (i.getId().equals(id)){

                return i;

            }
        }

        return null;

    }

    @Override
    public ComandasPagadasEntity dtoToEntity(ComandasPagadasDto comandasPagadasDto) {

        return new ComandasPagadasEntity(comandasPagadasDto.getId(), comandasPagadasDto.getMesa(), comandasPagadasDto.getCamarero(), comandasPagadasDto.getFecha(), comandasPagadasDto.getProductos(), comandasPagadasDto.getTotal());

    }

    @Override
    public ComandasPagadasResponse entityToResponse(ComandasPagadasEntity comandasPagadasEntity) {

        return new ComandasPagadasResponse(comandasPagadasEntity.getId(), comandasPagadasEntity.getMesa(), comandasPagadasEntity.getCamarero(), comandasPagadasEntity.getFecha(), comandasPagadasEntity.getProductos(), comandasPagadasEntity.getTotal());

    }

    @Override
    public List<ComandasPagadasResponse> listEntityToListResponse(List<ComandasPagadasEntity> list) {

        List<ComandasPagadasResponse> exit = new ArrayList<>();

        for (ComandasPagadasEntity i:list){

            exit.add(entityToResponse(i));

        }

        return exit;

    }

    @Override
    public List<ComandasPagadasResponse> filtro(ComandasPagadasDto comandasPagadasDto) {

        List<ComandasPagadasResponse> exit = new ArrayList<>();
        List<ComandasPagadasEntity> list = listAll();

        if (comandasPagadasDto.getMesa() == 0){
            if (comandasPagadasDto.getCamarero().equals("*")){
                if (comandasPagadasDto.getFecha().equals("")){

                    for (ComandasPagadasEntity i:list){

                        exit.add(entityToResponse(i));

                    }

                }else {

                    for (ComandasPagadasEntity i:list){
                        if (i.getFecha().equals(comandasPagadasDto.getFecha())){

                            exit.add(entityToResponse(i));

                        }
                    }

                }
            }else if (comandasPagadasDto.getFecha().equals("")){

                for (ComandasPagadasEntity i:list){
                    if (i.getCamarero().equals(comandasPagadasDto.getCamarero())){

                        exit.add(entityToResponse(i));

                    }
                }

            }else {

                for (ComandasPagadasEntity i:list){
                    if (i.getFecha().equals(comandasPagadasDto.getFecha())){
                        if (i.getCamarero().equals(comandasPagadasDto.getCamarero())){

                            exit.add(entityToResponse(i));

                        }
                    }
                }

            }
        }else if (comandasPagadasDto.getCamarero().equals("*")){
            if (comandasPagadasDto.getFecha().equals("")){

                for (ComandasPagadasEntity i:list){
                    if (i.getMesa() == comandasPagadasDto.getMesa()){

                        exit.add(entityToResponse(i));

                    }
                }

            }else {

                for (ComandasPagadasEntity i:list){
                    if (i.getFecha().equals(comandasPagadasDto.getFecha())){
                        if (i.getMesa() == comandasPagadasDto.getMesa()){

                            exit.add(entityToResponse(i));

                        }
                    }
                }

            }
        }else if (comandasPagadasDto.getFecha().equals("")){

            for (ComandasPagadasEntity i:list){
                if (i.getCamarero().equals(comandasPagadasDto.getCamarero())){
                    if (i.getMesa() == comandasPagadasDto.getMesa()){

                        exit.add(entityToResponse(i));

                    }
                }
            }

        }else {

            for (ComandasPagadasEntity i:list){
                if (i.getFecha().equals(comandasPagadasDto.getFecha())){
                    if (i.getCamarero().equals(comandasPagadasDto.getCamarero())){
                        if (i.getMesa() == comandasPagadasDto.getMesa()){

                            exit.add(entityToResponse(i));

                        }
                    }
                }
            }

        }

        return exit;

    }

    @Override
    public List<Integer> mesa(List<ComandasPagadasEntity> list) {

        List<Integer> exit = new ArrayList<>();

        for (int i = 0; i < list.size(); i++){

            boolean norepeti = true;

            if (i == 0){

                exit.add(list.get(i).getMesa());

            }else {

                int largo = exit.size();

                for (int j = 0; j < largo; j++){
                    if (exit.get(j) == list.get(i).getMesa()){

                        norepeti = false;

                    }
                }

                if (norepeti){

                    exit.add(list.get(i).getMesa());

                }

            }

        }

        return exit;

    }

    @Override
    public List<String> camarero(List<ComandasPagadasEntity> list) {

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
    public void manyDelete(ArrayList<String> id) {

        for (String i:id){

            delete(i);

        }

    }

}
