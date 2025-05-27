package org.demointernetshop55mfs.service;

import lombok.RequiredArgsConstructor;
import org.demointernetshop55mfs.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final ConfirmationCodeService confirmationCodeService;




}
