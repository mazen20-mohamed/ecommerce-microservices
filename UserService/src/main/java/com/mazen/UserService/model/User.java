package com.mazen.UserService.model;


import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class User extends DateEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
}
