package com.almatap.AlmatapBackend.controllers;

import com.almatap.AlmatapBackend.models.Event;
import com.almatap.AlmatapBackend.services.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {

    private final EventService eventService;

    public MainController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/mainPage")
    public String mainPage(Model model){
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
