package com.flashseat.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "password_resets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordReset {

    @Id
    private String id;

    private String userId;  // User.id as String

    @Indexed(unique = true)
    private String token;   // random token

    private LocalDateTime createdAt = LocalDateTime.now();

    // This is the correct way in Spring Boot 3.3+ 
    @Indexed(expireAfterSeconds = 900)  // Auto-delete after 15 minutes (900 seconds)
    private LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(15);

    private boolean used = false;
}