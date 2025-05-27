package org.demointernetshop55mfs.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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


    private Integer id;
    private String lastName;
    private String firstName;
    private String email;
    private String hashPassword;
    private Role role;
    private Status status;
    private String photoLink;




}
