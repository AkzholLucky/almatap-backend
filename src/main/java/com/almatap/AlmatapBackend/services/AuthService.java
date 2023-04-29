package com.almatap.AlmatapBackend.services;

import com.almatap.AlmatapBackend.models.User;
import com.almatap.AlmatapBackend.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class AuthService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderService mailSenderService;

    @Autowired
    public AuthService(UsersRepository usersRepository, PasswordEncoder passwordEncoder, MailSenderService mailSenderService) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSenderService = mailSenderService;
    }

    @Transactional
    public void userSave(User user){

        user.setActivationCode(UUID.randomUUID().toString());
        user.setEnabled(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");

        usersRepository.save(user);

        String message = String.format(
                "Hello, %s! \n" +
                        "Welcome to almatap. Please, visit next link to activate your account: https://almatap-backend.onrender.com/auth/activate/%s",
                user.getName(),
                user.getActivationCode()
        );

        mailSenderService.send(user.getEmail(), "Activation code", message);
    }

    @Transactional
    public boolean activateUser(String code) {
        User user = usersRepository.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);
        user.setEnabled(true);

        usersRepository.save(user);

        return true;
    }

    @Transactional
    public boolean isFound(String email){
        User user = usersRepository.findByEmail(email).orElse(null);

        if (user == null){
            return false;
        }

        String password = user.getName() + "123";

//        user.setActivationCode(UUID.randomUUID().toString());
//        usersRepository.save(user);

        String message = String.format(
                "Hello, %s! \n" +
                        "Your new password - %s",
                user.getName(),
                password
        );

        changePassword(user, user.getEmail(), password);
        mailSenderService.send(user.getEmail(), "New Password", message);
        return true;
    }

    public User findByCode(String code){
        return usersRepository.findByActivationCode(code);
    }

    public User findByEmail(String email){
        return usersRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public void changePassword(User user, String code, String password){
        User passwordChangedUser = findByEmail(code);

        passwordChangedUser.setPassword(passwordEncoder.encode(password));
//        passwordChangedUser.setActivationCode(null);

        usersRepository.save(passwordChangedUser);
    }
}