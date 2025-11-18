package edu.guvi.kaddysShop.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.guvi.kaddysShop.Model.User;
import edu.guvi.kaddysShop.Repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

   @Override
public User register(User user) {

    // Check if email exists already
    User existing = userRepo.findByEmail(user.getEmail());
    if (existing != null) {
        throw new RuntimeException("Email already exists!");
    }

    // Check if admin already exists
    if ("ADMIN".equalsIgnoreCase(user.getRole())) {

        long adminCount = userRepo.countByRole("ADMIN");

        if (adminCount >= 1) {
            throw new RuntimeException("Admin account already exists. Only one admin allowed.");
        }
    }

    // Default role assignment (if user doesn't manually set ADMIN)
    if (user.getRole() == null || user.getRole().isEmpty()) {
        user.setRole("CUSTOMER");
    }

    // Encode password
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    return userRepo.save(user);
}

    @Override
    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public User updateProfile(Long id, User updated) {

        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(updated.getName());
        user.setEmail(updated.getEmail());

        return userRepo.save(user);
    }
}
