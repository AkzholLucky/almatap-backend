package com.almatap.AlmatapBackend.repositories;

import com.almatap.AlmatapBackend.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
}
