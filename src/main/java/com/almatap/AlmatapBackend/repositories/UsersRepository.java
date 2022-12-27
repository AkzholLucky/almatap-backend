package com.almatap.AlmatapBackend.repositories;

import com.almatap.AlmatapBackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Component
public interface UsersRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    User findByActivationCode(String code);
}
