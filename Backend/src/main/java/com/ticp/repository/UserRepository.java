package com.ticp.repository;

import com.ticp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String>
{
    User findByEmail(String email);
    User findByUsername(String username);
}
