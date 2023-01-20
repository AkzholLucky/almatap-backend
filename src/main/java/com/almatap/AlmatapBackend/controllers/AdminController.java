package com.almatap.AlmatapBackend.controllers;

import com.almatap.AlmatapBackend.services.EventService;
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
    private List<String> category;

    public AdminController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping()
    public String adminPage(Model model) {

        model.addAttribute("categories", getCategory());
        return "admin/adminPage";
    }

    @PostMapping()
    public String addEvent(@RequestParam("file") MultipartFile file,
                           @RequestParam("name") String name,
                           @RequestParam("category") String category,
                           @RequestParam("desc") String desc) throws IOException {

        eventService.addEvent(file, name, desc, category);
        return "redirect:/mainPage";
    }

    //update
    @GetMapping("/change")
    public String changeAllEvents(Model model) {
        model.addAttribute("events", eventService.findAllEvent());
        return "admin/changeAll";
    }

    @GetMapping("/change/{id}")
    public String changeEvent(Model model, @PathVariable int id) {
        model.addAttribute("event", eventService.findOne(id));
        model.addAttribute("categories", getCategory());
        return "admin/changeOne";
    }

    @PostMapping("/change/{id}")
    public String saveChanges(@PathVariable int id,
                              @RequestParam("file") MultipartFile file,
                              @RequestParam("name") String name,
                              @RequestParam("category") String category,
                              @RequestParam(required = false, name = "desc") String desc) throws IOException {

        eventService.eventUpdate(file, name, desc, category, id);
        return "redirect:/adminPage";
    }

    //delete
    @GetMapping("/delete")
    public String deleteAllEvents(Model model) {
        model.addAttribute("events", eventService.findAllEvent());
        return "admin/deleteAll";
    }

    @GetMapping("/delete/{id}")
    public String deleteEvent(Model model, @PathVariable int id) {
        model.addAttribute("event", eventService.findOne(id));
        return "admin/deleteOne";
    }

    @PostMapping("/delete/{id}")
    public String deleteEventPost(@PathVariable int id) {
        eventService.deleteEvent(id);
        return "redirect:/adminPage";
    }

    private List<String> getCategory(){

        category = new ArrayList<>();
        category.add("Parks");
        category.add("Shopping centre");
        category.add("Entertainment center");
        category.add("Mountains");

        return category;
    }
}