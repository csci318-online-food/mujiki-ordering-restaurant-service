package com.csci318.microservice.restaurant.Entities.Relations;

import com.csci318.microservice.restaurant.Constants.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "username", length = 64)
    private String username;

    @Column(name = "password", length = 256)
    private String password;

    @Column(name = "firstname", length = 16)
    private String firstName;

    @Column(name = "lastname", length = 16)
    private String lastName;

    @Column(name = "email", length = 64)
    private String email;

    @Column(name = "phone", length = 16)
    private String phone;

    @Column(name = "roles")
    @Enumerated(EnumType.STRING)
    private Roles role;

    @Column(name = "create_at")
    private Timestamp createAt;

    @Column(name = "modify_at")
    private Timestamp modifyAt;

    @Column(name = "modify_by", length = 64)
    private String modifyBy;

    @Column(name = "create_by", length = 64)
    private String createBy;
}