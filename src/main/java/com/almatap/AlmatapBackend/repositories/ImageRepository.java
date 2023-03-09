package com.almatap.AlmatapBackend.repositories;

import com.almatap.AlmatapBackend.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    void deleteByEventId(int id);
}
