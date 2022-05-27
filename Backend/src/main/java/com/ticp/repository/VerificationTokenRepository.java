package com.ticp.repository;

import com.ticp.model.VerificationToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VerificationTokenRepository extends MongoRepository<VerificationToken, String>
{
    VerificationToken findByToken(String token);
}
