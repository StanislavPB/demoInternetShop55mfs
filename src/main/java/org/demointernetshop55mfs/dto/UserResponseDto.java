package org.demointernetshop55mfs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {

    private Integer id;
    private String lastName;
    private String firstName;
    private String email;
    private String role;

}
