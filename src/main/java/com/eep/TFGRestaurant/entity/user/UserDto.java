package com.eep.TFGRestaurant.entity.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotBlank(message = "El usuario no cumple los requisitos necesarios.")
    private String user;
    @NotBlank(message = "La contraseña no cumple los requisitos necesarios")
    private String password;
    private boolean admi;

}
