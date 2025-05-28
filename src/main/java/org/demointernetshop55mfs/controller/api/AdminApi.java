package org.demointernetshop55mfs.controller.api;


import org.demointernetshop55mfs.dto.UserResponseDto;
import org.demointernetshop55mfs.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/admin")
public interface AdminApi {

   // найти всех пользователей (полная информация - для ADMIN)
    @GetMapping("/users/fullDetails")
    public ResponseEntity<List<User>> findAllFullDetails();


    // найти всех пользователей (ограниченная информация - для MANAGER)
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> findAll();

    // удалить запись
    @DeleteMapping("/delete/{id}")
    public boolean deleteUser(@PathVariable Integer id);

}
