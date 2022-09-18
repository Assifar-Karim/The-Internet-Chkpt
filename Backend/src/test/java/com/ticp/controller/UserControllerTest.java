package com.ticp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ticp.dto.PasswordDTO;
import com.ticp.dto.UserDTO;
import com.ticp.error.exception.DuplicateUserException;
import com.ticp.error.exception.EmailNotFoundException;
import com.ticp.error.exception.TokenNotFoundException;
import com.ticp.error.handler.RestResponseEntityExceptionHandler;
import com.ticp.event.RegistrationCompleteEvent;
import com.ticp.model.PasswordToken;
import com.ticp.model.User;
import com.ticp.model.VerificationToken;
import com.ticp.service.EmailSenderService;
import com.ticp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest
{
    private MockMvc mockMvc;
    @Mock
    private UserService userService;
    @Mock
    private EmailSenderService emailSenderService;
    @Mock
    private ApplicationEventPublisher publisher;
    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setup()
    {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }
    @Test
    void shouldReturnRegistration() throws Exception
    {
        //GIVEN
        UserDTO userDTO = new UserDTO("main-character1","mc1@provider.com", "password");
        ObjectWriter objectWriter = new ObjectMapper().writer();
        String userDTOJson = objectWriter.writeValueAsString(userDTO);

        when(userService.registerUser(any(UserDTO.class))).thenReturn(new User(userDTO.getUsername(), userDTO.getEmail(), "encrypted-password"));
        doNothing().when(publisher).publishEvent(any(RegistrationCompleteEvent.class));

        //WHEN
        mockMvc.perform(post("/users").contentType("application/json").content(userDTOJson).characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
        //THEN
                .andExpect(status().isOk())
                .andExpect(content().string("Registration"));
    }

    @Test
    void shouldThrowDuplicateUserException() throws Exception
    {
        //GIVEN
        UserDTO userDTO = new UserDTO("main-character1","mc1@provider.com", "password");
        ObjectWriter objectWriter = new ObjectMapper().writer();
        String userDTOJson = objectWriter.writeValueAsString(userDTO);

        when(userService.registerUser(any(UserDTO.class))).thenThrow(new DuplicateUserException("An account for that username / email already exists."));

        //WHEN
        mockMvc.perform(post("/users").contentType("application/json").content(userDTOJson).characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
        //THEN
                .andExpect(status().isConflict())
                .andExpect(content().json("{\"status\": \"CONFLICT\", \"message\": \"An account for that username / email already exists.\"}"));
    }

    @Test
    void shouldReturnVerificationComplete() throws Exception
    {
        //GIVEN
        String verificationToken = "token";
        when(userService.validateVerificationToken(verificationToken)).thenReturn("Valid");

        //WHEN
        mockMvc.perform(get("/verifications?token=".concat(verificationToken)).characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
        //THEN
                .andExpect(status().isOk())
                .andExpect(content().string("Verification Complete"));
    }

    @Test
    void shouldReturnExpiredToken() throws Exception
    {
        //GIVEN
        String verificationToken = "expired-token";
        when(userService.validateVerificationToken(verificationToken)).thenReturn("Expired token");

        //WHEN
        mockMvc.perform(get("/verifications?token=".concat(verificationToken)).characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
        //THEN
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Expired token"));
    }

    @Test
    void shouldReturnInvalidToken() throws Exception
    {
        //GIVEN
        String verificationToken = "invalid-token";
        when(userService.validateVerificationToken(verificationToken)).thenReturn("Invalid token");

        //WHEN
        mockMvc.perform(get("/verifications?token=".concat(verificationToken)).characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
        //THEN
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid token"));
    }

    @Test
    void shouldReturnVerificationTokenRegenerated() throws Exception
    {
        //GIVEN
        String oldToken = "old-token";

        User mockUser = new User("username","username@mail-provider.com","password");
        VerificationToken verificationToken = new VerificationToken(oldToken);
        verificationToken.setUser(mockUser);

        when(userService.regenerateVerificationToken(oldToken)).thenReturn(verificationToken);
        doNothing().when(emailSenderService).sendMessageUsingThymeleafTemplate(anyString(), anyString(), anyMap(), anyString());

        //WHEN
        mockMvc.perform(get("/verifications/regen?token=".concat(oldToken)).characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
        //THEN
                .andExpect(status().isOk())
                .andExpect(content().string("Verification token regenerated"));
    }

    @Test
    void shouldThrowTokenNotFoundException() throws Exception
    {
        //GIVEN
        String oldToken = "old-token";
        when(userService.regenerateVerificationToken(oldToken)).thenThrow(new TokenNotFoundException("Non existing verification token"));
        //WHEN
        mockMvc.perform(get("/verifications/regen?token=".concat(oldToken)).characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
        //THEN
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"status\": \"NOT_FOUND\", \"message\": \"Non existing verification token\"}"));
    }

    @Test
    void shouldReturnPasswordResetTokenGenerated() throws Exception
    {
        //GIVEN
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setEmail("username@mail-provider.com");
        ObjectWriter objectWriter = new ObjectMapper().writer();
        String passwordDTOJson = objectWriter.writeValueAsString(passwordDTO);
        User mockUser = new User("username","username@mail-provider.com","password");

        when(userService.generatePasswordToken(any(PasswordDTO.class))).thenReturn(new PasswordToken("token", mockUser));
        doNothing().when(emailSenderService).sendMessageUsingThymeleafTemplate(anyString(), anyString(), anyMap(), anyString());

        //WHEN
        mockMvc.perform(post("/users/passwords/reset").contentType("application/json").content(passwordDTOJson).characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
        //THEN
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset token generated"));
    }

    @Test
    void shouldThrowEmptyOrNullEmailException() throws Exception
    {
        //GIVEN
        PasswordDTO passwordDTO = new PasswordDTO();
        ObjectWriter objectWriter = new ObjectMapper().writer();
        String passwordDTOJson = objectWriter.writeValueAsString(passwordDTO);

        //WHEN
        mockMvc.perform(post("/users/passwords/reset").contentType("application/json").content(passwordDTOJson).characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
        //THEN
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"status\": \"BAD_REQUEST\", \"message\": \"Please input a valid email\"}"));
    }

    @Test
    void shouldThrowEmailNotFoundException() throws Exception
    {
        //GIVEN
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setEmail("username@mail-provider.com");
        ObjectWriter objectWriter = new ObjectMapper().writer();
        String passwordDTOJson = objectWriter.writeValueAsString(passwordDTO);

        when(userService.generatePasswordToken(any(PasswordDTO.class))).thenThrow(new EmailNotFoundException("Email not found"));

        //WHEN
        mockMvc.perform(post("/users/passwords/reset").contentType("application/json").content(passwordDTOJson).characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
        //THEN
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"status\": \"NOT_FOUND\", \"message\": \"Email not found\"}"));
    }

    @Test
    void shouldReturnPasswordVerificationComplete() throws Exception
    {
        //GIVEN
        String verificationToken = "token";
        when(userService.validatePasswordToken(verificationToken)).thenReturn("Valid");

        //WHEN
        mockMvc.perform(get("/users/passwords/verifications?token=".concat(verificationToken)).characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
        //THEN
                .andExpect(status().isOk())
                .andExpect(content().string("Verification Complete"));
    }

    @Test
    void shouldReturnExpiredPasswordToken() throws Exception
    {
        //GIVEN
        String verificationToken = "expired-token";
        when(userService.validatePasswordToken(verificationToken)).thenReturn("Expired token");

        //WHEN
        mockMvc.perform(get("/users/passwords/verifications?token=".concat(verificationToken)).characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
        //THEN
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Expired token"));
    }

    @Test
    void shouldReturnInvalidPasswordToken() throws Exception
    {
        //GIVEN
        String verificationToken = "invalid-token";
        when(userService.validatePasswordToken(verificationToken)).thenReturn("Invalid token");

        //WHEN
        mockMvc.perform(get("/users/passwords/verifications?token=".concat(verificationToken)).characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
        //THEN
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid token"));
    }

    @Test
    void shouldReturnPasswordResetSuccessfully() throws Exception
    {
        //GIVEN
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setToken("token");
        passwordDTO.setNewPassword("new password");
        ObjectWriter objectWriter = new ObjectMapper().writer();
        String passwordDTOJson = objectWriter.writeValueAsString(passwordDTO);

        doNothing().when(userService).changePassword(any(PasswordDTO.class));

        //WHEN
        mockMvc.perform(post("/users/passwords").contentType("application/json").content(passwordDTOJson).characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
        //THEN
                .andExpect(status().isOk())
                .andExpect(content().string("Password Reset Successfully"));
    }

    @Test
    void shouldThrowEmptyOrNullTokenException() throws Exception
    {
        //GIVEN
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setNewPassword("new password");
        ObjectWriter objectWriter = new ObjectMapper().writer();
        String passwordDTOJson = objectWriter.writeValueAsString(passwordDTO);

        //WHEN
        mockMvc.perform(post("/users/passwords").contentType("application/json").content(passwordDTOJson).characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
        //THEN
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"status\": \"BAD_REQUEST\", \"message\": \"Please input a valid token\"}"));
    }

    @Test
    void shouldThrowEmptyOrNullPasswordException() throws Exception
    {
        //GIVEN
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setToken("token");
        ObjectWriter objectWriter = new ObjectMapper().writer();
        String passwordDTOJson = objectWriter.writeValueAsString(passwordDTO);

        //WHEN
        mockMvc.perform(post("/users/passwords").contentType("application/json").content(passwordDTOJson).characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
        //THEN
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"status\": \"BAD_REQUEST\", \"message\": \"Please input a valid password\"}"));
    }

    @Test
    void shouldThrowPasswordTokenNotFoundException() throws Exception
    {
        //GIVEN
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setToken("token");
        passwordDTO.setNewPassword("new password");
        ObjectWriter objectWriter = new ObjectMapper().writer();
        String passwordDTOJson = objectWriter.writeValueAsString(passwordDTO);

        doThrow(new TokenNotFoundException("Invalid token")).when(userService).changePassword(any(PasswordDTO.class));

        //WHEN
        mockMvc.perform(post("/users/passwords").contentType("application/json").content(passwordDTOJson).characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
        //THEN
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"status\": \"NOT_FOUND\", \"message\": \"Invalid token\"}"));
    }

    @Test
    void shouldThrowPasswordEmailNotFoundException() throws Exception
    {
        //GIVEN
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setToken("token");
        passwordDTO.setNewPassword("new password");
        ObjectWriter objectWriter = new ObjectMapper().writer();
        String passwordDTOJson = objectWriter.writeValueAsString(passwordDTO);

        doThrow(new EmailNotFoundException("Email not found")).when(userService).changePassword(any(PasswordDTO.class));

        //WHEN
        mockMvc.perform(post("/users/passwords").contentType("application/json").content(passwordDTOJson).characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
        //THEN
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"status\": \"NOT_FOUND\", \"message\": \"Email not found\"}"));
    }

}