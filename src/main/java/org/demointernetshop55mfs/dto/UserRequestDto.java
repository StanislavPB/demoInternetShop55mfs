package org.demointernetshop55mfs.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDto {

    @NotBlank(message = "Last name is required and must be not blank Controller Validation")
    @Size(min = 3, max = 20, message = "Last name length not correct")
    private String lastName;

    @NotBlank (message = "First name is required and must be not blank Controller Validation")
    @Size(min = 3, max = 20, message = "First name length not correct")
    private String firstName;

    @Email(message = "email должен соответствовать стандарту Controller Validation")
    private String email;


    @NotBlank (message = "Password is required and must be not blank Controller Validation")
    @Size(min = 8, max = 20, message = "Password length not correct Controller Validation")
    @Pattern(regexp = "[A-Za-z0-9!]+", message = "Пароль может содержать только буквы, цифры и знак '!' Controller Validation")
    private String hashPassword;


}
