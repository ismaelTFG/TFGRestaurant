package com.eep.TFGRestaurant.service;

import com.eep.TFGRestaurant.entity.user.UserDto;
import com.eep.TFGRestaurant.entity.user.UserEntity;
import com.eep.TFGRestaurant.entity.user.UserResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface UserService {

    public abstract List<UserEntity> listAll() throws ExecutionException, InterruptedException;
    public abstract boolean add(UserEntity userEntity);
    public abstract boolean delete(String id);
    public abstract boolean update(UserEntity userEntity);
    public abstract List<UserEntity> listAllSinUser(UserResponse userResponse);
    public abstract UserEntity findByUser(String user);

    public abstract UserEntity DtoToEntity(UserDto userDto);
    public abstract UserResponse entityToResponse(UserEntity userEntity);
    public abstract List<UserResponse> listEntityToListRespnse(List<UserEntity> list);

    public abstract boolean userNoRepetidos(UserEntity userEntity);
    public abstract boolean validar(UserEntity userEntity);
    public abstract boolean manyDelete(ArrayList<String> id);

}
