package com.scurtis.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInformation {
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
}
