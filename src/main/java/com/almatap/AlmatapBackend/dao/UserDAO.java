package com.almatap.AlmatapBackend.dao;

import com.almatap.AlmatapBackend.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserDAO {
    private final JdbcTemplate jdbcTemplate;

    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//    public void updateUser(User user, String activationCode){
//        jdbcTemplate.update("UPDATE users SET year_of_birth=?, activation_code=?, email=?, is_enabled=?, name=?, password=?, surname=?, username=? WHERE activation_code=?",
//                user.getYearOfBirth(), user.getActivationCode(), user.getEmail(), user.isEnabled(), user.getName(), user.getPassword(), user.getSurname(), user.getUsername(), activationCode);
//    }
}
