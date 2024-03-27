package com.csye6225.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "first_name", nullable = false)
    @NotBlank
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank
    private String lastName;

    @Column(name = "password", nullable = false)
    @NotBlank
    private String password;

    @Column(nullable = false, unique = true)
    @NotBlank
    private String username;

    @Column(name = "account_created", updatable = false)
    @CreationTimestamp
    private Date accountCreated;

    @Column(name = "account_updated")
    @UpdateTimestamp
    private Date accountUpdated;

    @Column(name = "isVerified")
    private boolean isVerified;

    @CreationTimestamp
    @Column(name = "verificationMailSentTime")
    private Date verificationMailSentTime;
}


