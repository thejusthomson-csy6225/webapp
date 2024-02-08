package com.csye6225.model;

import lombok.Data;

import java.util.Date;

@Data
public class UserResponseDTO {

    private String id;

    private String firstName;

    private String lastName;

    private String username;

    private Date accountCreated;

    private Date accountUpdated;

}
