package com.ticp.service;

import com.ticp.dto.PasswordDTO;
import com.ticp.dto.UserDTO;
import com.ticp.error.exception.DuplicateUserException;
import com.ticp.error.exception.EmailNotFoundException;
import com.ticp.error.exception.TokenNotFoundException;
import com.ticp.mapper.ConcreteMapperFactory;
import com.ticp.mapper.PasswordMapper;
import com.ticp.mapper.UserMapper;
import com.ticp.model.PasswordToken;
import com.ticp.model.User;
import com.ticp.model.VerificationToken;
import com.ticp.repository.PasswordTokenRepository;
import com.ticp.repository.UserRepository;
import com.ticp.repository.VerificationTokenRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService, UserDetailsService
{
    private static Logger logger = LogManager.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConcreteMapperFactory mapperFactory;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private PasswordTokenRepository passwordTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserDTO userDTO) throws DuplicateUserException
    {
        UserMapper userMapper = (UserMapper) mapperFactory.getMapper(User.class);
        User user = userMapper.toModel(userDTO);
        try
        {
            userRepository.save(user);
        }
        catch (DuplicateKeyException e)
        {
            throw new DuplicateUserException("An account for that username / email already exists.");
        }
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
            return "Expired token";
        }
        user.setActive(true);
        userRepository.save(user);
        return "Valid";
    }

    @Override
    public VerificationToken regenerateVerificationToken(String oldToken) throws TokenNotFoundException
    {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
        if(verificationToken == null)
        {
            throw new TokenNotFoundException("Non existing verification token");
        }
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    @Override
    public PasswordToken generatePasswordToken(PasswordDTO passwordDTO) throws EmailNotFoundException
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
        throw new EmailNotFoundException("Email not found");
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
            return "Expired token";
        }
        return "Valid";
    }

    @Override
    public void changePassword(PasswordDTO passwordDTO) throws EmailNotFoundException, TokenNotFoundException
    {
        PasswordToken passwordToken = passwordTokenRepository.findByToken(passwordDTO.getToken());
        Calendar calendar = Calendar.getInstance();
        if(passwordToken == null || passwordToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0)
        {
            throw new TokenNotFoundException("Invalid Token");
        }
        User user = passwordToken.getUser();
        if(user == null)
        {
            throw new EmailNotFoundException("Email not found");
        }
        PasswordMapper passwordMapper = (PasswordMapper) mapperFactory.getMapper(PasswordToken.class);
        userRepository.save(passwordMapper.toModel(passwordDTO, user));
    }

    @Override
    public User findUserByEmail(String email)
    {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userRepository.findByUsername(username);
        if(user == null)
        {

            logger.error("User {} was not found in the database", username);
            throw new UsernameNotFoundException("User not found");
        }
        logger.info("User {} was found in the users' database", username);
        Collection<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole()));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isActive(),
                true,
                true,
                true,
                authorities
        );
    }

    @Override
    public User findUserByUsername(String username)
    {
        return userRepository.findByUsername(username);
    }
}
