package com.ticp.service;

import com.ticp.dto.PasswordDTO;
import com.ticp.dto.UserDTO;
import com.ticp.mapper.ConcreteMapperFactory;
import com.ticp.mapper.PasswordMapper;
import com.ticp.mapper.UserMapper;
import com.ticp.model.PasswordToken;
import com.ticp.model.User;
import com.ticp.model.VerificationToken;
import com.ticp.repository.PasswordTokenRepository;
import com.ticp.repository.UserRepository;
import com.ticp.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConcreteMapperFactory mapperFactory;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private PasswordTokenRepository passwordTokenRepository;

    @Override
    public User registerUser(UserDTO userDTO)
    {
        UserMapper userMapper = (UserMapper) mapperFactory.getMapper(User.class);
        User user = userMapper.toModel(userDTO);
        userRepository.save(user);
        return user;
    }

    @Override
    public void saveVerificationTokenForUser(String token, User user)
    {
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token)
    {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if(verificationToken == null)
        {
            return "Invalid token";
        }
        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if(verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0)
        {
            verificationTokenRepository.delete(verificationToken);
            return "Expired token";
        }
        user.setActive(true);
        userRepository.save(user);
        return "Valid";
    }

    @Override
    public VerificationToken regenerateVerificationToken(String oldToken)
    {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
        if(verificationToken != null)
        {
            verificationToken.setToken(UUID.randomUUID().toString());
            verificationTokenRepository.save(verificationToken);
        }
        return verificationToken;
    }

    @Override
    public PasswordToken generatePasswordToken(PasswordDTO passwordDTO)
    {
        PasswordToken passwordToken = passwordTokenRepository.findByUserEmail(passwordDTO.getEmail());
        if(passwordToken != null)
        {
            String token = UUID.randomUUID().toString();
            passwordToken.setToken(token);
            passwordTokenRepository.save(passwordToken);
            return passwordToken;
        }
        User user = userRepository.findByEmail(passwordDTO.getEmail());
        if(user != null)
        {
            String token = UUID.randomUUID().toString();
            passwordToken = new PasswordToken(token, user);
            passwordTokenRepository.save(passwordToken);
            return passwordToken;
        }
        return null;
    }

    @Override
    public String validatePasswordToken(String token)
    {
        PasswordToken passwordToken = passwordTokenRepository.findByToken(token);
        if(passwordToken == null)
        {
            return "Invalid token";
        }
        Calendar calendar = Calendar.getInstance();
        if(passwordToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0)
        {
            passwordTokenRepository.delete(passwordToken);
            return "Expired token";
        }
        return "Valid";
    }

    @Override
    public User getUserByPasswordToken(String token)
    {
        return passwordTokenRepository.findByToken(token).getUser();
    }

    @Override
    public void changePassword(User user, PasswordDTO passwordDTO)
    {
        PasswordMapper passwordMapper = (PasswordMapper) mapperFactory.getMapper(PasswordToken.class);
        userRepository.save(passwordMapper.toModel(passwordDTO, user));
    }

    @Override
    public User findUserByEmail(String email)
    {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findUserByUsername(String username)
    {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean verifyOldPassword(User user, String oldPassword)
    {
        // NOTE (KARIM) : This verification will have to be changed when the encryption mechanism is added
        return user.getPassword().equals(oldPassword);
    }
}
