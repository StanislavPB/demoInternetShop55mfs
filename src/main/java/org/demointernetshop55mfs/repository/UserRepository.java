package org.demointernetshop55mfs.repository;

import org.demointernetshop55mfs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    // задекларируем метод который должен ответить на вопрос а есть ли у нас зарегистрированный
    // пользователь с таким-то email

    boolean existsByEmail(String email);

    // метод для ответа на вопрос "предоставьте данные о пользователе с таким-то email"
    Optional<User> findByEmail(String email);

    List<User> findByLastName(String lastName);

    // этот метод будет возвращать коллекцию, которая будет состоять из
    // записей в которых поле last_name будет совпадать с тем, что нам передали
    // в переменной lastName

}
