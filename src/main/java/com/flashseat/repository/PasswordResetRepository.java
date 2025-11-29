package com.flashseat.repository;

import com.flashseat.model.PasswordReset;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface PasswordResetRepository extends MongoRepository<PasswordReset, String> {
    Optional<PasswordReset> findByToken(String token);
}