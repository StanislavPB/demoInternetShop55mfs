package org.demointernetshop55mfs.controller;

import org.demointernetshop55mfs.entity.ConfirmationCode;
import org.demointernetshop55mfs.entity.User;
import org.demointernetshop55mfs.repository.ConfirmationCodeRepository;
import org.demointernetshop55mfs.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationCodeRepository confirmationCodeRepository;

    @BeforeEach
    void setUp() {
        User testUser = new User();
        testUser.setFirstName("user1");
        testUser.setLastName("user1");
        testUser.setEmail("user1@gmail.com");
        testUser.setHashPassword("Pass12345!");
        testUser.setRole(User.Role.USER);
        testUser.setStatus(User.Status.NOT_CONFIRMED);
        User savedUser = userRepository.save(testUser);

        ConfirmationCode code = new ConfirmationCode();
        code.setCode("someConfirmationCode");
        code.setUser(savedUser);
        code.setExpireDataTime(LocalDateTime.now().plusDays(1));
        confirmationCodeRepository.save(code);
    }

    @AfterEach
    void drop() {
        confirmationCodeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetUserByIdWhenIDCorrect() throws Exception {
        mockMvc.perform(get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("user1@gmail.com"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetUserByIdWhenIDNotExist() throws Exception {
        String errorMessage = "User with ID = 2 not found";

        mockMvc.perform(get("/api/users/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetUserByIdWhenIDNotCorrect() throws Exception {
        mockMvc.perform(get("/api/users/2a")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid parameter"))
                .andExpect(jsonPath("$.parameter").value("id"))
                .andExpect(jsonPath("$.rejectedValue").value("2a"));
    }
}
