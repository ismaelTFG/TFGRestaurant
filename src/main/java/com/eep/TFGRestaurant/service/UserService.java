package com.eep.TFGRestaurant.service;

import com.eep.TFGRestaurant.entity.user.UserDto;
import com.eep.TFGRestaurant.entity.user.UserEntity;

import java.util.List;

public interface UserService {

    public abstract List<UserEntity> listAll();
    public abstract boolean add(UserEntity userEntity);
    public abstract boolean update(String id, UserEntity userEntity);
    public abstract boolean delete(String id);

    public abstract UserEntity DtoToEntity(UserDto userDto);

    public abstract boolean validar(UserEntity userEntity);

}
