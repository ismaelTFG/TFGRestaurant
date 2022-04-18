package com.eep.TFGRestaurant.serviceImpl;


import com.eep.TFGRestaurant.entity.user.UserDto;
import com.eep.TFGRestaurant.entity.user.UserEntity;
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
    public UserEntity DtoToEntity(UserDto userDto) {

        return new UserEntity(userDto.getUser(), userDto.getPassword(), userDto.isAdmi());

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
}
