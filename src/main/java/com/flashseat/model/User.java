package com.flashseat.model;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Document(collection = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank(message = "Student ID is required")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Student ID must be uppercase letters and numbers only")
    private String studentId;

    @NotBlank
    private String name;

    @Indexed(unique = true)
    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 10, max = 10)
    private String phone;

    @NotBlank
    private String password;   // <-- Important: actual password field

    @NotBlank
    private String department;

    @NotBlank
    @Pattern(regexp = "[1-4]", message = "Year must be 1-4")
    private String year;

    public enum Gender {
        MALE, FEMALE
    }

    @NotNull
    private Gender gender;

    private boolean isActive = true;

    private LocalDateTime createdAt = LocalDateTime.now();

    private String createdBy;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_STUDENT"));
    }

    @Override
    public String getUsername() {
        return studentId;
    }

    // REMOVE THE BAD OVERRIDE
    // Spring Security will now use Lombok's getter for "password"

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
