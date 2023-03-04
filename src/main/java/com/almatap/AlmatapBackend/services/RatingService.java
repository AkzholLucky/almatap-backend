package com.almatap.AlmatapBackend.services;

import com.almatap.AlmatapBackend.models.Event;
import com.almatap.AlmatapBackend.models.Rating;
import com.almatap.AlmatapBackend.models.User;
import com.almatap.AlmatapBackend.repositories.RatingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RatingService {

    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Transactional
    public void saveRating(Rating rating){
        ratingRepository.save(rating);
    }

    @Transactional
    public void saveUpdatedRating(Rating currentRating, Rating updatedRating){
        currentRating.setRating(updatedRating.getRating());
        currentRating.setEvent(updatedRating.getEvent());
        currentRating.setUser(updatedRating.getUser());
        currentRating.setDatePost(updatedRating.getDatePost());
    }

    public boolean isDidRating(User user, Event event){
        List<Rating> ratingList = ratingRepository.findByUser(user);

        if (ratingList != null){

            for (Rating rating : ratingList) {

                if (rating.getEvent().getId() == event.getId()) {
                    return true;
                }
            }

        }

        return false;
    }

    public double averageRating(Event event){

        List<Rating> ratingList = ratingRepository.findByEvent(event);
        double averageRating = 0;

        if (!ratingList.isEmpty()){
            for (Rating rating : ratingList){
                averageRating = averageRating + rating.getRating();
            }
            return averageRating/ratingList.size();
        }
        return 5.0;
    }

    public Rating ratingByEventAndUser(Event event, User user){
        return ratingRepository.findRatingByEventAndUser(event, user);
    }
}
