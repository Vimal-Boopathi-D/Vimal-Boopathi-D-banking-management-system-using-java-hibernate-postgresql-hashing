package com.bank.service;

import com.bank.dao.UserDAO;
import com.bank.entity.User;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();

    public void register(String username, String password, String role) {
        if (userDAO.findByUsername(username) != null) {
            throw new RuntimeException("Username already exists");
        }
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(username, hashed, role);
        userDAO.save(user);
    }

    public User login(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user == null) return null;
        if (BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}
