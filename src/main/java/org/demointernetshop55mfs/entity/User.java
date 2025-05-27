package org.demointernetshop55mfs.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "account")
public class User {
    public enum Role {
        ADMIN,
        USER,
        MANAGER
    }

    public enum Status {
        NOT_CONFIRMED,
        CONFIRMED,
        BANNED,
        DELETE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String lastName;
    private String firstName;
    private String email;
    private String hashPassword;
    @Enumerated (value = EnumType.STRING)
    private Role role;
    @Enumerated(value = EnumType.STRING)
    private Status status;
    private String photoLink;




}
