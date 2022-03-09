package com.scurtis.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginCredentials {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
