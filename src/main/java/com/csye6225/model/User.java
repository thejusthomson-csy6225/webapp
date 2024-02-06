package com.csye6225.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;


@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "account_created", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date accountCreated;

    @Column(name = "account_updated")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date accountUpdated;
}


