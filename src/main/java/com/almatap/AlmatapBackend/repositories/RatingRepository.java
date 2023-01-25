package com.almatap.AlmatapBackend.repositories;

import com.almatap.AlmatapBackend.models.Event;
import com.almatap.AlmatapBackend.models.Rating;
import com.almatap.AlmatapBackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {
    List<Rating> findByUser(User user);
    List<Rating> findByEvent(Event event);
}
