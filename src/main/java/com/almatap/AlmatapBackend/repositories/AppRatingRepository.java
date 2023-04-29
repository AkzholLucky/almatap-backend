package com.almatap.AlmatapBackend.repositories;

import com.almatap.AlmatapBackend.models.AppRating;
import com.almatap.AlmatapBackend.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppRatingRepository extends JpaRepository<AppRating, Integer> {
    Optional<AppRating> findByUserId(int id);
}
