package com.ticp.service;

import com.ticp.dto.PasswordDTO;
import com.ticp.error.exception.EmailNotFoundException;
import com.ticp.error.exception.TokenNotFoundException;
import com.ticp.model.PasswordToken;
import com.ticp.model.User;
import com.ticp.model.VerificationToken;
import com.ticp.repository.PasswordTokenRepository;
import com.ticp.repository.UserRepository;
import com.ticp.repository.VerificationTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest
{
    @Mock
    private UserRepository userRepository;
    @Mock
    private VerificationTokenRepository verificationTokenRepository;
    @Mock
    private PasswordTokenRepository passwordTokenRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldReturnValid()
    {
        // GIVEN
        String token = "token";
        VerificationToken verificationToken = new VerificationToken("token", new User());
        verificationToken.setExpirationTime(new Date(Calendar.getInstance().getTime().getTime() + 100));
        when(verificationTokenRepository.findByToken(token)).thenReturn(verificationToken);

        // WHEN
        var actualResult = userService.validateVerificationToken(token);
        // THEN
        assertEquals("Valid", actualResult);
    }
    @Test
    void shouldReturnInvalid()
    {
        // GIVEN
        String token = "token";
        when(verificationTokenRepository.findByToken(token)).thenReturn(null);

        // WHEN
        var actualResult = userService.validateVerificationToken(token);
        // THEN
        assertEquals("Invalid token", actualResult);
    }
    @Test
    void shouldReturnExpired()
    {
        // GIVEN
        String token = "token";
        VerificationToken verificationToken = new VerificationToken("token", new User());
        verificationToken.setExpirationTime(new Date(Calendar.getInstance().getTime().getTime() - 100));
        when(verificationTokenRepository.findByToken(token)).thenReturn(verificationToken);

        // WHEN
        var actualResult = userService.validateVerificationToken(token);
        // THEN
        assertEquals("Expired token", actualResult);
    }

    @Test
    void shouldThrowTokenNotFoundException1() throws TokenNotFoundException {
        // GIVEN
        String token = "token";
        when(verificationTokenRepository.findByToken(token)).thenReturn(null);

        // WHEN
        var actualException = assertThrows(TokenNotFoundException.class, () -> userService.regenerateVerificationToken(token));
        // THEN
        assertEquals("Non existing verification token", actualException.getMessage());
    }

    @Test
    void shouldThrowTokenNotFoundException2()
    {
        //GIVEN
        String token = "token";
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setToken(token);
        PasswordToken expectedToken = new PasswordToken(token);
        expectedToken.setExpirationTime(new Date(Calendar.getInstance().getTime().getTime() - 100));
        when(passwordTokenRepository.findByToken(token)).thenReturn(expectedToken);

        //WHEN
        TokenNotFoundException actualException = assertThrows(TokenNotFoundException.class,() -> {
            userService.changePassword(passwordDTO);
        });
        //THEN
        assertEquals("Invalid Token", actualException.getMessage());
    }

    @Test
    void shouldThrowEmailNotFoundException()
    {
        //GIVEN
        String token = "token";
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setToken(token);
        PasswordToken expectedToken = new PasswordToken(token);
        expectedToken.setExpirationTime(new Date(Calendar.getInstance().getTime().getTime() + 100));
        when(passwordTokenRepository.findByToken(token)).thenReturn(expectedToken);

        //WHEN
        EmailNotFoundException actualException = assertThrows(EmailNotFoundException.class,() -> {
            userService.changePassword(passwordDTO);
        });
        //THEN
        assertEquals("Email not found", actualException.getMessage());
    }

    @Test
    void shouldReturnExistingUser1()
    {
        // GIVEN
        String email = "mc1@provider.com";
        User expectedResult = new User("main-character", "mc1@provider.com", "password");
        expectedResult.setId("id");
        when(userRepository.findByEmail(email)).thenReturn(expectedResult);

        // WHEN
        var actualResult = userService.findUserByUsername(email);
        // THEN
        assertEquals(expectedResult.getId(), actualResult.getId());
    }

    @Test
    void shouldReturnNullUser1()
    {
        // GIVEN
        String email = "username@provider.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        // WHEN
        var actualResult = userService.findUserByEmail(email);
        // THEN
        assertNull(actualResult);
    }

    @Test
    void shouldReturnExistingUser2()
    {
        //GIVEN
        String username = "main-character";
        User expectedResult = new User("main-character", "mc1@provider.com", "password");
        expectedResult.setId("id");
        when(userRepository.findByUsername(username)).thenReturn(expectedResult);

        // WHEN
        var actualResult = userService.findUserByUsername(username);
        // THEN
        assertEquals(expectedResult.getId(), actualResult.getId());
    }

    @Test
    void shouldReturnNullUser2()
    {
        // GIVEN
        String username = "username";
        when(userRepository.findByUsername(username)).thenReturn(null);
        // WHEN
        var actualResult = userService.findUserByUsername(username);
        // THEN
        assertNull(actualResult);
    }
}