package org.demointernetshop55mfs.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ConfirmationCode {

    private Integer id;

    private String code;

    private User user;

    private LocalDateTime expireDataTime;

    private boolean isConfirmed;

}
