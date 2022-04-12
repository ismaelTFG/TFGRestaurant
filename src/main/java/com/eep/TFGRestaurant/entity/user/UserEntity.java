package com.eep.TFGRestaurant.entity.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    private String id;
    private String user;
    private String password;
    private boolean admi;

    public UserEntity(String user, String password, boolean admi) {

        this.user = user;
        this.password = password;
        this.admi = admi;

    }
}
