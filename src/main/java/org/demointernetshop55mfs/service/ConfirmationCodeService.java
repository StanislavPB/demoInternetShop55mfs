package org.demointernetshop55mfs.service;


import lombok.RequiredArgsConstructor;
import org.demointernetshop55mfs.entity.ConfirmationCode;
import org.demointernetshop55mfs.entity.User;
import org.demointernetshop55mfs.repository.ConfirmationCodeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmationCodeService {

    private final ConfirmationCodeRepository repository;

    private final int EXPIRATION_PERIOD = 1;

    private final String LINK_PATH = "localhost:8080/api/confirmation?code=";


    public void confirmationCodeHandle(User user){
        // создать код
        String code = generateCode();
        // сохранить код в репозитории и таблице БД
        saveConfirmationCode(code, user);
        // отправить по почте
        sendCodeByEmail(code,user);
    }

    private String generateCode(){

        /*
        UUID - universal uniq identifier
        формат 128-bit
        xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
        где каждый символ 'x' - это либо цифра либо символ от a-f
        3f29c3b2-9fc2-11ed-a8fc-0242ac120002
         */

        String code = UUID.randomUUID().toString();
        return code;

    }

    private void saveConfirmationCode(String codeUUID, User user){
//        ConfirmationCode newCode = new ConfirmationCode();
//        newCode.setCode(codeUUID);
//        newCode.setUser(user);
//        newCode.setExpireDataTime(LocalDateTime.now().plusDays(EXPIRATION_PERIOD));
//        newCode.setConfirmed(false);

        ConfirmationCode newCode = ConfirmationCode.builder()
                .code(codeUUID)
                .user(user)
                .expireDataTime(LocalDateTime.now().plusDays(EXPIRATION_PERIOD))
                .isConfirmed(false)
                .build();

        repository.save(newCode);
    }

    private void sendCodeByEmail(String code, User user){
        String linkToSend = LINK_PATH + code;

        System.out.println("Отправили на почту код подтверждения: " + linkToSend);


    }

    public User confirmUserByCode(String code){
        ConfirmationCode confirmationCode = repository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Confirmation code: " + code + " not found" ));

        User user = confirmationCode.getUser();

        confirmationCode.setConfirmed(true);
        repository.save(confirmationCode);

        return user;
    }


}
