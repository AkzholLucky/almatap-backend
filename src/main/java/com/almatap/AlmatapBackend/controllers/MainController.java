package com.almatap.AlmatapBackend.controllers;

import com.almatap.AlmatapBackend.models.Event;
import com.almatap.AlmatapBackend.models.Image;
import com.almatap.AlmatapBackend.models.Rating;
import com.almatap.AlmatapBackend.models.User;
import com.almatap.AlmatapBackend.security.UsersDetails;
import com.almatap.AlmatapBackend.services.EventService;
import com.almatap.AlmatapBackend.services.ImageService;
import com.almatap.AlmatapBackend.services.RatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class MainController {

    private final EventService eventService;
    private final RatingService ratingService;
    private final ImageService imageService;

    public MainController(EventService eventService, RatingService ratingService, ImageService imageService) {
        this.eventService = eventService;
        this.ratingService = ratingService;
        this.imageService = imageService;
    }

    @GetMapping("/mainPage")
    public List<Event> mainPage(@RequestParam(value = "rating", defaultValue = "0.0") double rating,
                                @RequestParam(value = "min", defaultValue = "0") int min,
                                @RequestParam(value = "max", defaultValue = "1000000") int max){

        return eventService.findAllWithRatingFilter(rating, min, max);
    }

    @GetMapping("/event/{id}")
    public Event event(@PathVariable("id") int id){
//        Event event = eventService.findOne(id);
//        Map<String, Object> map = new HashMap<>();
////        boolean isDid = ratingService.isDidRating(currentUser(), event);
//
//        model.addAttribute("event", event);
//        map.put("Message", "This email already exist!");
////        model.addAttribute("isDid", isDid);
////        model.addAttribute("images", event.getImages());
        return eventService.findOne(id);
    }

    @GetMapping("/images")
    public List<Image> getImage(){
        return imageService.findAll();
    }

    @PostMapping("/event/{id}")
    public Map<String, String> rateEvent(@PathVariable int id,
                         @RequestParam("rating") String rateStr,
                         @RequestParam(required = false, defaultValue="ratingAgain") String ratingAgain){
        Rating rating = new Rating();
        Event event = eventService.findOne(id);

        rating.setRating(Integer.parseInt(rateStr));
        rating.setDatePost(new Date());
        rating.setEvent(event);
        rating.setUser(currentUser());

        if (ratingAgain.equals("true")){

            Rating currentRating = ratingService.ratingByEventAndUser(event, currentUser());
            ratingService.saveUpdatedRating(currentRating, rating);

        } else {
            ratingService.saveRating(rating);
        }

        return Map.of("Message", "Successfully rated!");
    }

    @GetMapping("/search")
    public ResponseEntity<HttpStatus> searchPage(){
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/search")
    public List<Event> search(@RequestParam("name") String name){
        return eventService.findByNameStartsWith(name);
    }

    private User currentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsersDetails usersDetails = (UsersDetails) authentication.getPrincipal();
        return usersDetails.getUser();
    }
}