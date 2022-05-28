package com.ticp.service;

import com.ticp.model.User;
import com.ticp.model.VerificationToken;
import com.ticp.repository.UserRepository;
import com.ticp.repository.VerificationTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest
{
    @Mock
    private UserRepository userRepository;
    @Mock
    private VerificationTokenRepository verificationTokenRepository;
    @InjectMocks
    private UserServiceImpl userService;

    // validateVerificationToken() Tests
    @Test
    void given_valid_token_validateVerificationToken_should_return_valid()
    {
        // GIVEN
        String token = "token";
        VerificationToken verificationToken = new VerificationToken("token", new User());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);
        verificationToken.setExpirationTime(calendar.getTime());
        // WHEN
        when(verificationTokenRepository.findByToken(token)).thenReturn(verificationToken);
        // THEN
        assertTrue(userService.validateVerificationToken(token).equalsIgnoreCase("valid"));
    }
    @Test
    void given_non_existing_token_validateVerificationToken_should_return_invalid()
    {
        // GIVEN
        String token = "token";
        // WHEN
        when(verificationTokenRepository.findByToken(token)).thenReturn(null);
        // THEN
        assertTrue(userService.validateVerificationToken(token).equalsIgnoreCase("invalid token"));

    }
    @Test
    void given_expired_token_validateVerificationToken_should_return_expired()
    {
        // GIVEN
        String token = "token";
        VerificationToken verificationToken = new VerificationToken("token", new User());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -10);
        verificationToken.setExpirationTime(calendar.getTime());
        // WHEN
        when(verificationTokenRepository.findByToken(token)).thenReturn(verificationToken);
        // THEN
        assertTrue(userService.validateVerificationToken(token).equalsIgnoreCase("expired token"));
    }

    // regenerateVerificationToken() Tests
    @Test
    void given_non_existing_old_token_regenerateVerificationToken_should_return_null()
    {
        // GIVEN
        String oldToken = "oldToken";
        // WHEN
        when(verificationTokenRepository.findByToken(oldToken)).thenReturn(null);
        // THEN
        assertNull(userService.regenerateVerificationToken(oldToken));
    }

    // findUserByEmail() Tests
    @Test
    void given_existing_email_findUserByEmail_should_return_user()
    {
        // GIVEN
        String email = "username@mail-provider.com";
        // WHEN
        when(userRepository.findByEmail(email)).thenReturn(new User("username", email, "secret_password"));
        // THEN
        User foundUser = userService.findUserByEmail(email);
        assertTrue(foundUser.getUsername().equalsIgnoreCase("username") &&
                foundUser.getEmail().equalsIgnoreCase(email));
    }
    @Test
    void given_non_existing_email_findUserByEmail_should_return_user()
    {
        // GIVEN
        String email = "username@mail-provider.com";
        // WHEN
        when(userRepository.findByEmail(email)).thenReturn(null);
        // THEN
        assertNull(userService.findUserByEmail(email));
    }

    // loadUserByUsername() Tests
    @Test
    void given_existing_username_loadUserByUsername_should_return_user_details()
    {
        // GIVEN
        String username = "username";
        // WHEN
        when(userRepository.findByUsername(username)).thenReturn(new User("username",
                "username@mail-provider.com", "secret_password"));
        // THEN
        assertTrue(userService.loadUserByUsername(username).getUsername().equalsIgnoreCase(username));
    }

    // findUserByUsername() Tests
    @Test
    void given_existing_username_findUserByUsername_should_return_user()
    {
        String username = "username";
        // WHEN
        when(userRepository.findByUsername(username)).thenReturn(new User("username",
                "username@mail-provider.com", "secret_password"));
        // THEN
        User foundUser = userService.findUserByUsername(username);
        assertTrue(foundUser.getUsername().equalsIgnoreCase(username) &&
                foundUser.getEmail().equalsIgnoreCase("username@mail-provider.com"));
    }
    @Test
    void given_non_existing_username_findUserByUsername_should_return_null()
    {
        // GIVEN
        String username = "username";
        // WHEN
        when(userRepository.findByUsername(username)).thenReturn(null);
        // THEN
        assertNull(userService.findUserByUsername(username));
    }
}