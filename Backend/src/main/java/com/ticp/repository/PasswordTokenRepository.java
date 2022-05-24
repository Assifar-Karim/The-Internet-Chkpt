package com.ticp.repository;

import com.ticp.model.PasswordToken;
import com.ticp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface PasswordTokenRepository extends MongoRepository<PasswordToken, String>
{
    PasswordToken findByToken(String token);
    @Query(value = "{ 'user.email' : ?0 }")
    PasswordToken findByUserEmail(String email);
}
