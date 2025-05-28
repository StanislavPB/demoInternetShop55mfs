package org.demointernetshop55mfs.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

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

    @NotBlank (message = "Last name is required and must be not blank")
    @Size(min = 3, max = 20, message = "Last name length not correct")
    private String lastName;

    @NotBlank (message = "First name is required and must be not blank")
    @Size(min = 3, max = 20, message = "First name length not correct")
    private String firstName;

    @Email(message = "email должен соответствовать стандарту")
    private String email;

    @NotBlank (message = "Password is required and must be not blank")
    @Size(min = 8, max = 20, message = "Password length not correct")
    @Pattern(regexp = "[A-Za-z0-9!]+", message = "Пароль может содержать только буквы, цифры и знак '!'")
    private String hashPassword;

    @Enumerated (value = EnumType.STRING)
    private Role role;
    @Enumerated(value = EnumType.STRING)
    private Status status;
    private String photoLink;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FileInfo> files = new HashSet<>();


}
