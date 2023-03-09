package com.almatap.AlmatapBackend.services;

import com.almatap.AlmatapBackend.models.User;
import com.almatap.AlmatapBackend.repositories.UsersRepository;
import com.almatap.AlmatapBackend.security.UsersDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
@Transactional(readOnly = true)
public class UsersService {

    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Transactional
    public void saveChanges(User user, MultipartFile file) throws IOException {
        User changedUser = currentUser();

        changedUser.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
        changedUser.setName(user.getName());
        changedUser.setSurname(user.getSurname());
        changedUser.setYearOfBirth(user.getYearOfBirth());

        usersRepository.save(changedUser);
    }

    @Transactional
    public void deleteUser(User user){
        usersRepository.delete(user);
    }

    private User currentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsersDetails usersDetails = (UsersDetails) authentication.getPrincipal();
        return usersDetails.getUser();
    }
}
