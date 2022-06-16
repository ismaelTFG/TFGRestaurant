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

import javax.print.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service("comandasServiceImpl")
public class ComandasServiceImpl implements ComandasService {

    //conexion con la base de datos
    @Autowired
    @Qualifier("fireBase")
    private FireBase fireBase;

    //servicio de productos
    @Autowired
    @Qualifier("productosServiceImpl")
    private ProductosService productosService;

    //servicio de comandas pagadas
    @Autowired
    @Qualifier("comandasPagadasServiceImpl")
    private ComandasPagadasService comandasPagadasService;

    /**
     * lista de todas las comandas
     * @return lista
     */
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

    /**
     * añadir comanda
     * @param comandasEntity comanda
     * @return si se guarda o no
     */
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

    /**
     * borrado de comanda
     * @param id de la comanda a borrar
     * @return si se borrar a no
     */
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

    /**
     * modificacion de comanda
     * @param comandasEntity comanda a modificar
     * @return si se modifica o no
     */
    @Override
    public boolean update(ComandasEntity comandasEntity) {

        ComandasEntity id = findByMesa(comandasEntity.getMesa());

        if(comandasEntity.getCamarero() == null){

            comandasEntity.setCamarero(id.getCamarero());

        }

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

    /**
     * buscar comanda por mesa
     * @param mesa a buscar
     * @return comanda
     */
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

    /**
     * añadir producto a comanda
     * @param comandasDTO comanda
     * @param seleccionados productos a añadir
     * @return si se añaden o no
     */
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

    /**
     * eliminacion de producto a comanda
     * @param comandasDTO comanda
     * @param seleccionados productos a eliminar
     * @return si se borran o no
     */
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

    /**
     * casteo de dto a entity
     * @param comandasDto comanda en dto
     * @return comanda en entity
     */
    @Override
    public ComandasEntity DtoToEntity(ComandasDTO comandasDto) {

        return new ComandasEntity(comandasDto.getMesa(), comandasDto.getCamarero(), comandasDto.getFecha(), comandasDto.getProductos());

    }

    /**
     * casteo de entity a response
     * @param comandasEntity comanda entity
     * @return comanda response
     */
    @Override
    public ComandasResponse EntityToResponse(ComandasEntity comandasEntity) {

        return new ComandasResponse(comandasEntity.getMesa(), comandasEntity.getCamarero(), comandasEntity.getFecha(), comandasEntity.getProductos());

    }

    /**
     * lista entity a lista response
     * @param list entity
     * @return lista response
     */
    @Override
    public List<ComandasResponse> listEntityToListResponse(List<ComandasEntity> list) {

        List<ComandasResponse> exit = new ArrayList<>();

        for (ComandasEntity i:list){

            exit.add(EntityToResponse(i));

        }

        return exit;

    }

    /**
     * paso de comanda sin pagar a comanda pagada
     * @param comandasEntity comanda sin pagar
     * @return comanda pagada
     */
    @Override
    public ComandasPagadasEntity sinPagarToPagada(ComandasEntity comandasEntity) {

        return new ComandasPagadasEntity(comandasEntity.getMesa(), comandasEntity.getCamarero(), comandasEntity.getFecha(), comandasEntity.getProductos(), total(comandasEntity));

    }

    /**
     * comprobacion de las mesas
     * @param comandasEntity comanda
     * @return si esta repetida o no
     */
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

    /**
     * lista de productos en una comandas
     * @param comandasEntity comanda
     * @return lista de productos
     */
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

    /**
     * lista de las mesas en las comandas
     * @param list de comandas
     * @return lista de mesas
     */
    @Override
    public List<Integer> mesa(List<ComandasEntity> list) {

        List<Integer> exit = new ArrayList<>();

        for (ComandasEntity i:list){

            exit.add(i.getMesa());

        }

        return exit;

    }

    /**
     * lista de camareros en las comandas
     * @param list de comandas
     * @return lista de camareros
     */
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

    /**
     * busqueda de comandas
     * @param comandasEntity parametros de busqueda
     * @return lista de comandas
     */
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

    /**
     * borrado de varias comandas
     * @param mesa lista de comandas
     */
    @Override
    public void manyDelete(ArrayList<Integer> mesa) {

        for (int i:mesa){

            delete(i);

        }

    }

    /**
     * calculo del total de la comanda
     * @param comandasEntity comanda
     * @return total
     */
    @Override
    public double total(ComandasEntity comandasEntity) {

        double exit = 0;
        List<ProductosEntity> list = productosService.listAll();

        for (String i : comandasEntity.getProductos()){
            for (ProductosEntity j : list){
                if (j.getId().equals(i)){

                    exit = exit + j.getPrecio();

                }
            }
        }

        return exit;

    }

    /**
     * generacion del ticket de una comanda Y imprimir esta
     * @param comandasEntity comanda
     * @return si se genero o no
     */
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

    /**
     * metodo que cambia la comanda de no pagada a pagada
     * @param comandasEntity comanda
     * @return si se guarda o no
     */
    @Override
    public boolean pagada(ComandasEntity comandasEntity) {

        comandasPagadasService.add(sinPagarToPagada(comandasEntity));
        delete(comandasEntity.getMesa());

        return false;

    }

    /**
     * metodo que imprime el ticket
     */
    @Override
    public void imprimir() {

        

    }

    /**
     * metodo para cambiar el encabezado
     * @return si se ha cambiado
     */
    @Override
    public boolean cambiarEncabezado() {

        return false;

    }

}
