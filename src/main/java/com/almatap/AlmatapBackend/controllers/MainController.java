package com.almatap.AlmatapBackend.controllers;

import com.almatap.AlmatapBackend.dto.UserDTO;
import com.almatap.AlmatapBackend.models.Event;
import com.almatap.AlmatapBackend.models.User;
import com.almatap.AlmatapBackend.security.UsersDetails;
import com.almatap.AlmatapBackend.services.EventService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {

    private final EventService eventService;
    private final ModelMapper modelMapper;

    public MainController(EventService eventService, ModelMapper modelMapper) {
        this.eventService = eventService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/mainPage")
    public String mainPage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsersDetails usersDetails = (UsersDetails) authentication.getPrincipal();
        model.addAttribute("user", usersDetails.getUser());
        model.addAttribute("events", eventService.findAllEvent());
        return "main/mainPage";
    }

    @GetMapping("/event/{id}")
    public String event(@PathVariable("id") int id, Model model){
        Event event = eventService.findOne(id);
        model.addAttribute("event", event);
        return "main/event";
    }
}
