package com.eep.TFGRestaurant.service;

import com.eep.TFGRestaurant.entity.user.UserDto;
import com.eep.TFGRestaurant.entity.user.UserEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface UserService {

    public abstract List<UserEntity> listAll() throws ExecutionException, InterruptedException;
    public abstract boolean add(UserEntity userEntity);
    public abstract boolean delete(String id);
    public abstract boolean update(UserEntity userEntity);

    public abstract UserEntity DtoToEntity(UserDto userDto);

    public abstract boolean validar(UserEntity userEntity);

}
