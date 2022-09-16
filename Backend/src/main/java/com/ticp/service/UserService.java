package com.ticp.service;

import com.ticp.dto.PasswordDTO;
import com.ticp.dto.UserDTO;
import com.ticp.error.exception.DuplicateUserException;
import com.ticp.error.exception.EmailNotFoundException;
import com.ticp.error.exception.TokenNotFoundException;
import com.ticp.model.PasswordToken;
import com.ticp.model.User;
import com.ticp.model.VerificationToken;

public interface UserService
{
    User registerUser(UserDTO userDTO) throws DuplicateUserException;
    void saveVerificationTokenForUser(String token, User user);
    String validateVerificationToken(String token);
    VerificationToken regenerateVerificationToken(String oldToken) throws TokenNotFoundException;
    PasswordToken generatePasswordToken(PasswordDTO passwordDTO) throws EmailNotFoundException;
    String validatePasswordToken(String token);
    void changePassword(PasswordDTO passwordDTO) throws EmailNotFoundException, TokenNotFoundException;
    User findUserByEmail(String email);
    User findUserByUsername(String username);
}
