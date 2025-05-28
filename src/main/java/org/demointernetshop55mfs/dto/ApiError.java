package org.demointernetshop55mfs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class ApiError {
    private String error;
    // Общее описание ошибки (например, "Validation failed", "User not found")
    private String message;
    // Детализированное сообщение (например, "Пользователь с ID = 2 не найден")
    private String parameter;
    // Название параметра, если ошибка связана с ним (например, "userId")
    private Object rejectedValue;
    // Значение, которое вызвало ошибку
    private List<Map<String, Object>> errors;
    // Список ошибок валидации (если есть)
    private LocalDateTime timestamp;
    // Время ошибки
}
