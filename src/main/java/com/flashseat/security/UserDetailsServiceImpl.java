package com.flashseat.security;

import com.flashseat.model.Admin;
import com.flashseat.model.User;
import com.flashseat.repository.AdminRepository;
import com.flashseat.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public UserDetailsServiceImpl(UserRepository userRepository, AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	// Try Student first
        var studentOpt = userRepository.findByStudentId(username);
        if (studentOpt.isPresent()) {
            User user = studentOpt.get();
            System.out.println("DEBUG: Found user with studentId: " + user.getStudentId());
            System.out.println("DEBUG: Loaded password: '" + user.getPassword() + "' (length: " + (user.getPassword() != null ? user.getPassword().length() : 0) + ")");
            return user;
        }

        // Then Admin
        var adminOpt = adminRepository.findByAdminId(username);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            System.out.println("DEBUG: Found admin with adminId: " + admin.getAdminId());
            System.out.println("DEBUG: Loaded password: '" + admin.getPassword() + "' (length: " + (admin.getPassword() != null ? admin.getPassword().length() : 0) + ")");
            return admin;
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}