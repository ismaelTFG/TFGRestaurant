package com.eep.TFGRestaurant.serviceImpl;


import com.eep.TFGRestaurant.entity.user.UserDto;
import com.eep.TFGRestaurant.entity.user.UserEntity;
import com.eep.TFGRestaurant.entity.user.UserResponse;
import com.eep.TFGRestaurant.repository.FireBase;
import com.eep.TFGRestaurant.service.UserService;
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

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    @Qualifier("fireBase")
    private FireBase fireBase;

    @Override
    public List<UserEntity> listAll() {

        List<UserEntity> list = new ArrayList<>();
        UserEntity userEntity;

        CollectionReference users = fireBase.getFirestore().collection("user");
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = users.get();

        try {

            for (DocumentSnapshot i: querySnapshotApiFuture.get().getDocuments()) {

                userEntity = i.toObject(UserEntity.class);
                userEntity.setUser(i.getId());

                list.add(userEntity);

            }

        } catch (InterruptedException | ExecutionException e) {

            e.printStackTrace();

        }

        return list;

    }

    @Override
    public boolean add(UserEntity userEntity) {

        Map<String, Object> docData = new HashMap<>();

        docData.put("password", userEntity.getPassword());
        docData.put("admi", userEntity.isAdmi());

        CollectionReference users = fireBase.getFirestore().collection("user");
        ApiFuture<WriteResult> writeResultApiFuture = users.document(userEntity.getUser()).create(docData);

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

        CollectionReference users = fireBase.getFirestore().collection("user");
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
    public boolean update(UserEntity userEntity) {

        Map<String, Object> docData = new HashMap<>();

        docData.put("password", userEntity.getPassword());
        docData.put("admi", userEntity.isAdmi());

        CollectionReference users = fireBase.getFirestore().collection("user");
        ApiFuture<WriteResult> writeResultApiFuture = users.document(userEntity.getUser()).set(docData);

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
    public List<UserEntity> listAllSinUser(UserResponse userResponse) {

        List<UserEntity> list = listAll();

        for (int i = 0; i < list.size(); i++){
            if (list.get(i).getUser().equals(userResponse.getUser())){

                list.remove(i);

            }
        }

        return list;
    }

    @Override
    public UserEntity findByUser(String user) {

        List<UserEntity> list = listAll();

        for (UserEntity i:list){
            if (i.getUser().equals(user)){

                return i;

            }
        }

        return null;
    }

    @Override
    public UserEntity DtoToEntity(UserDto userDto) {

        return new UserEntity(userDto.getUser(), userDto.getPassword(), userDto.isAdmi());

    }

    @Override
    public UserResponse entityToResponse(UserEntity userEntity) {

        return new UserResponse(userEntity.getUser(), userEntity.getPassword(), userEntity.isAdmi());

    }

    @Override
    public List<UserResponse> listEntityToListRespnse(List<UserEntity> list) {

        List<UserResponse> exit = new ArrayList<>();

        for (UserEntity i:list){

            exit.add(entityToResponse(i));

        }

        return exit;

    }

    @Override
    public boolean userNoRepetidos(UserEntity userEntity) {

        List<UserEntity> list = listAll();

        for (UserEntity i:list){
            if (i.getUser().equals(userEntity.getUser())){

                return true;

            }
        }

        return false;

    }

    @Override
    public boolean validar(UserEntity userEntity) {

        List<UserEntity> list = listAll();

        for (UserEntity i:list) {
            if (i.getUser().equals(userEntity.getUser())){
                if (i.getPassword().equals(userEntity.getPassword())){

                    return true;

                }
            }
        }

        return false;

    }

    @Override
    public boolean manyDelete(ArrayList<String> id) {

        List<UserEntity> list = listAll();
        int admi = 0;

        for (UserEntity i:list){
            if (i.isAdmi()){

                admi++;

            }
        }

        for (UserEntity i:list){
            for (String j:id){
                if (i.getUser().equals(j)){
                    if (i.isAdmi()){

                        admi = admi -1;

                    }
                }
            }
        }

        if (admi > 0){

            for (String i:id){

                delete(i);

            }

            return true;

        }

        return false;

    }

}
