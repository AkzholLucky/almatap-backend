package com.almatap.AlmatapBackend.controllers;

import com.almatap.AlmatapBackend.services.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/adminPage")
public class AdminController {
    private final EventService eventService;

    public AdminController(EventService eventService) {
        this.eventService = eventService;
    }

    private final List<String> category = new ArrayList<>();

    @GetMapping()
    public String adminPage(Model model){
        category.add("first");
        category.add("second");
        category.add("third");
        category.add("fourth");
        model.addAttribute("categories", category);
        return "admin/adminPage";
    }

    @PostMapping()
    public String addEvent(@RequestParam("file") MultipartFile file,
                           @RequestParam("name") String name,
                           @RequestParam(value = "category", defaultValue = "first") String category,
                           @RequestParam("desc") String desc) throws IOException {

        System.out.println("Hello world!");
        eventService.addEvent(file, name, desc, category);
        return "redirect:/mainPage";
    }
}