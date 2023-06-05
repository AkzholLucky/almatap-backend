package com.almatap.AlmatapBackend.controllers;

import com.almatap.AlmatapBackend.models.*;
import com.almatap.AlmatapBackend.security.UsersDetails;
import com.almatap.AlmatapBackend.services.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/main")
public class MainController {

    private final EventService eventService;
    private final RatingService ratingService;
    private final ImageService imageService;
    private final MailSenderService mailSenderService;
    private final AppRatingService appRatingService;

    public MainController(EventService eventService, RatingService ratingService, ImageService imageService, MailSenderService mailSenderService, AppRatingService appRatingService) {
        this.eventService = eventService;
        this.ratingService = ratingService;
        this.imageService = imageService;
        this.mailSenderService = mailSenderService;
        this.appRatingService = appRatingService;
    }

    @GetMapping("/mainPage")
    public List<Event> mainPage(@RequestParam(value = "rating", defaultValue = "0.0") double rating,
                                @RequestParam(value = "min", defaultValue = "0") int min,
                                @RequestParam(value = "max", defaultValue = "1000000") int max,
                                @RequestParam(value = "city", defaultValue = "default") String city,
                                @RequestParam(value = "eventId", defaultValue = "0") int eventId,
                                @RequestParam(value = "category", defaultValue = "all") String category){

        if (eventId != 0){
            eventService.addOrDeleteFavorites(currentUser(), eventService.findOne(eventId));
        }

        return eventService.findAllWithRatingFilter(rating, min, max, city, category);
    }

    @PostMapping("/subscribe")
    public Map<String, String> subscription(@RequestParam(value = "email") String email){

        String message = String.format(
                "Hello, %s! \n" +
                        "You subscribe to our news! Then wait Digests from us!",
                currentUser().getName()
        );

        mailSenderService.send(email, "You Successfully subscribe", message);
        return Map.of("Message", "Successfully subscribe!");
    }

    @PostMapping("/appRating")
    public Map<String, String> appRating(@RequestParam("rating") String rateStr){
        AppRating appRating = new AppRating();

        appRating.setRating(Integer.parseInt(rateStr));
        appRating.setDatePost(new Date());
        appRating.setUser(currentUser());

        if (appRatingService.findByUserId(currentUser().getId()).isPresent()){

            appRatingService.saveUpdatedRating(appRatingService.findByUserId(currentUser().getId()).get(), appRating);

        } else {
            appRatingService.saveRating(appRating);
        }

        return Map.of("Message", "Successfully rated!");

    }

    @GetMapping("/favorites")
    public List<Favorite> favorites(){
        return eventService.getAllFavoritesOfUser(currentUser().getId());
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

    @GetMapping("/notifications")
    public List<String> notifications(){
        return AdminController.notifications;
    }

    @GetMapping("/rating")
    public double avgRating(){
        return appRatingService.averageRating();
    }

    private User currentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsersDetails usersDetails = (UsersDetails) authentication.getPrincipal();
        return usersDetails.getUser();
    }
}