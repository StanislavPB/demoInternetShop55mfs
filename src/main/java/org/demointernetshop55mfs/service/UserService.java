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


    @Transactional
    public UserResponseDto confirmationEmail(String code){
        User user = confirmationCodeService.confirmUserByCode(code);
        user.setStatus(User.Status.CONFIRMED);
        repository.save(user);

        return converter.toDto(user);
    }


    public UserResponseDto updateUser(UserUpdateRequestDto updateRequest) {
        if (updateRequest.getEmail() == null || updateRequest.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email must be provided to update user");
        }

        String userEmail = updateRequest.getEmail();

        // найдем пользователя по email

        User userByEmail = repository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User with email: " + userEmail + " not found"));

        // обновляем все доступные поля
        // мы заранее НЕ ЗНАЕМ, а какие именно поля пользователь хочет поменять
        //то есть в JSON (в теле запроса) будут (могут быть) ТОЛЬКО те поля(со значениеми)
        // которые пользователь хочет менять (не обязательно все)

        if (updateRequest.getFirstName() != null && !updateRequest.getFirstName().isBlank()) {
            userByEmail.setFirstName(updateRequest.getFirstName());
        }

        if (updateRequest.getLastName() != null && !updateRequest.getLastName().isBlank()) {
            userByEmail.setLastName(updateRequest.getLastName());
        }

        if (updateRequest.getHashPassword() != null && !updateRequest.getHashPassword().isBlank()) {
            userByEmail.setHashPassword(updateRequest.getHashPassword());
        }

        // сохраняем (обновляем) пользователя

        repository.save(userByEmail);

        return converter.toDto(userByEmail);
        // или вручную создать UserResponseDto из данных, которые хранятся в userByEmail

    }

    public boolean deleteUser(Integer id) {
        // проверяем, что такой id существует
        // и если нет - то сразу возвращаем false и ничего не пытаемся делать
//
//        if (!repository.existsById(id)) {
//            return false;
//        }


        if (!repository.existsById(id)) {
            throw new NotFoundException("Удалить пользователя с ID: " + id + " невозможно. Пользователь не найден");
        }

        // если пользователь существует, то
        // вариант 1 - удаляем сразу по id

        repository.deleteById(id);

        // вариант 2 - сперва найдем объект по этому id

//        User userForDelete = repository.findById(id).get();
//
//        repository.delete(userForDelete);

        // такой вариант может быть востребован в том случае, если
        // мы будем возвращать не true / false, а ТОТ объект который мы удалили
        // это иногда бывает необходимо для дополнительного контроля операций

        return true;


    }

}
