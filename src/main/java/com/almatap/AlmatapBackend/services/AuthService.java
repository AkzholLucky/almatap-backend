package com.almatap.AlmatapBackend.services;

import com.almatap.AlmatapBackend.models.User;
import com.almatap.AlmatapBackend.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
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

        usersRepository.save(user);

        String message = String.format(
                "Hello, %s! \n" +
                        "Welcome to almatap. Please, visit next link: http://localhost:8080/auth/activate/%s",
                user.getUsername(),
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
        User user = usersRepository.findByEmail(email);

        if (user == null){
            return false;
        }

        user.setActivationCode(UUID.randomUUID().toString());
        usersRepository.save(user);

        String message = String.format(
                "Hello, %s! \n" +
                        "To change your password, Please, visit next link: http://localhost:8080/auth/lost-password/%s",
                user.getUsername(),
                user.getActivationCode()
        );

        mailSenderService.send(user.getEmail(), "To change your password", message);
        return true;
    }

    public User findByCode(String code){
        return usersRepository.findByActivationCode(code);
    }

    public User findByEmail(String email){
        return usersRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String username){
        return usersRepository.findByUsername(username);
    }

    @Transactional
    public void changePassword(User user, String code){
        User passwordChangedUser = findByCode(code);

        passwordChangedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        passwordChangedUser.setActivationCode(null);

        usersRepository.save(passwordChangedUser);
    }
}