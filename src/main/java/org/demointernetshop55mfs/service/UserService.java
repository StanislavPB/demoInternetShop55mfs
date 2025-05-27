package org.demointernetshop55mfs.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.demointernetshop55mfs.dto.UserRequestDto;
import org.demointernetshop55mfs.dto.UserResponseDto;
import org.demointernetshop55mfs.dto.UserUpdateRequestDto;
import org.demointernetshop55mfs.entity.User;
import org.demointernetshop55mfs.repository.UserRepository;
import org.demointernetshop55mfs.service.exception.AlreadyExistException;
import org.demointernetshop55mfs.service.exception.NotFoundException;
import org.demointernetshop55mfs.service.util.Converter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final ConfirmationCodeService confirmationCodeService;
    private final Converter converter;


    @Transactional
    public UserResponseDto registration(UserRequestDto request){
        if (repository.existsByEmail(request.getEmail())){
            throw new AlreadyExistException("User with email: " + request.getEmail() + " is already exist");
        }

        // ----- а вот если такого пользователя еще нет ------

        User newUser = converter.fromDto(request);
        newUser.setRole(User.Role.USER); // по умолчанию даем пользователю роль USER
        newUser.setStatus(User.Status.NOT_CONFIRMED);

        repository.save(newUser);

        // процедура создания нового пользователя включает в себя отправку кода подтверждения

        confirmationCodeService.confirmationCodeHandle(newUser);

        UserResponseDto response = converter.toDto(newUser);

        return response;

    }


    public List<UserResponseDto> findAllUsers(){
        List<User> users = repository.findAll();
        List<UserResponseDto> response = converter.fromUsers(users);
        return response;
    }

    public UserResponseDto findUserById(Integer id){
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with ID = " + id + " not found"));

        return converter.toDto(user);
    }

    public List<User> findFullDetailsUsers(){
        return repository.findAll();
    }

    public UserResponseDto findUserByEmail(String email){
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email = " + email + " not found"));

        return converter.toDto(user);
    }

//    public UserResponseDto updateUser(UserUpdateRequestDto updateRequest){
//        if ()
//    }

}
