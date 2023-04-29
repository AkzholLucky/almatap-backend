package com.almatap.AlmatapBackend.services;

import com.almatap.AlmatapBackend.models.AppRating;
import com.almatap.AlmatapBackend.models.Event;
import com.almatap.AlmatapBackend.models.Rating;
import com.almatap.AlmatapBackend.repositories.AppRatingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AppRatingService {

    private final AppRatingRepository appRatingRepository;

    public AppRatingService(AppRatingRepository appRatingRepository) {
        this.appRatingRepository = appRatingRepository;
    }

    public Optional<AppRating> findByUserId(int id){
        return appRatingRepository.findByUserId(id);
    }

    @Transactional
    public void saveUpdatedRating(AppRating currentRating, AppRating updatedRating){
        currentRating.setRating(updatedRating.getRating());
        currentRating.setUser(updatedRating.getUser());
        currentRating.setDatePost(updatedRating.getDatePost());
    }

    @Transactional
    public void saveRating(AppRating rating){
        appRatingRepository.save(rating);
    }

    public List<AppRating> findAll(){
        return appRatingRepository.findAll();
    }

    public double averageRating(){

        double averageRating = 0;

        if (!findAll().isEmpty()){
            for (AppRating rating : findAll()){
                averageRating = averageRating + rating.getRating();
            }
            return averageRating/findAll().size();
        }
        return 5.0;
    }
}