package com.almatap.AlmatapBackend.services;

import com.almatap.AlmatapBackend.models.User;
import com.almatap.AlmatapBackend.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderService mailSenderService;

    @Autowired
    public UsersService(UsersRepository usersRepository, PasswordEncoder passwordEncoder, MailSenderService mailSenderService) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSenderService = mailSenderService;
    }

    public User findById(int id){
        Optional<User> user = usersRepository.findById(id);
        return user.stream().findAny().orElse(null);
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

    public List<User> findAll(){
        return usersRepository.findAll();
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
}
