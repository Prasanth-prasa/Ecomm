package edu.guvi.kaddysShop.Service;

import edu.guvi.kaddysShop.Model.User;

public interface UserService {

    User register(User user);

    User findByEmail(String email);

    User updateProfile(Long id, User updated);
}
