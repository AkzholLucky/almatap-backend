package com.almatap.AlmatapBackend.controllers;

import com.almatap.AlmatapBackend.models.Event;
import com.almatap.AlmatapBackend.models.Rating;
import com.almatap.AlmatapBackend.models.User;
import com.almatap.AlmatapBackend.security.UsersDetails;
import com.almatap.AlmatapBackend.services.EventService;
import com.almatap.AlmatapBackend.services.RatingService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class MainController {

    private final EventService eventService;
    private final RatingService ratingService;

    public MainController(EventService eventService, RatingService ratingService) {
        this.eventService = eventService;
        this.ratingService = ratingService;
    }

    @GetMapping("/mainPage")
    public String mainPage(Model model){
        model.addAttribute("user", currentUser());
        model.addAttribute("events", eventService.findAllEvent());
        return "main/mainPage";
    }

    @GetMapping("/event/{id}")
    public String event(@PathVariable("id") int id, Model model){
        Event event = eventService.findOne(id);
        boolean isDid = ratingService.isDidRating(currentUser(), event);
        double averageRating = ratingService.averageRating(event);

        model.addAttribute("event", event);
        model.addAttribute("isDid", isDid);
        model.addAttribute("averageRating", averageRating);
        return "main/event";
    }

    @PostMapping("/event/{id}")
    public String rateEvent(@PathVariable int id, @RequestParam("rating") String rateStr){
        Rating rating = new Rating();

        rating.setRating(Integer.parseInt(rateStr));
        rating.setDatePost(new Date());
        rating.setEvent(eventService.findOne(id));
        rating.setUser(currentUser());

        ratingService.saveRating(rating);

        return "redirect:/event/" + id;
    }

    private User currentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsersDetails usersDetails = (UsersDetails) authentication.getPrincipal();
        return usersDetails.getUser();
    }
}
