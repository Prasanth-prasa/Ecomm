package edu.guvi.kaddysShop.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import edu.guvi.kaddysShop.Model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    long countByRole(String role);

}
