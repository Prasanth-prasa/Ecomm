package edu.guvi.kaddysShop.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import edu.guvi.kaddysShop.Model.User;
import edu.guvi.kaddysShop.Repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository repo;  // MUST BE final
    private final PasswordEncoder passwordEncoder; // MUST BE final

    @Override
    public void run(String... args) throws Exception {

        if (repo.countByRole("ADMIN") == 0) {

            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@kaddys.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");

            repo.save(admin);

            System.out.println("Admin user created automatically");
        }
    }
}
