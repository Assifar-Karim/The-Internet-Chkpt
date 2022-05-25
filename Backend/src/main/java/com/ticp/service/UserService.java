package com.ticp.service;

import com.ticp.dto.PasswordDTO;
import com.ticp.dto.UserDTO;
import com.ticp.model.PasswordToken;
import com.ticp.model.User;
import com.ticp.model.VerificationToken;

import java.util.Optional;

public interface UserService
{
    User registerUser(UserDTO userDTO);
    void saveVerificationTokenForUser(String token, User user);
    String validateVerificationToken(String token);
    VerificationToken regenerateVerificationToken(String oldToken);
    PasswordToken generatePasswordToken(PasswordDTO passwordDTO);
    String validatePasswordToken(String token);
    User getUserByPasswordToken(String token);
    void changePassword(User user, PasswordDTO passwordDTO);
    User findUserByEmail(String email);
    Optional<User> findUserByUsername(String username);
    boolean verifyOldPassword(User user, String oldPassword);
    User findUserByUsername(String username);
}
