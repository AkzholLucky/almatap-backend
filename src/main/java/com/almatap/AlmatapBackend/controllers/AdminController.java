package com.almatap.AlmatapBackend.controllers;

import com.almatap.AlmatapBackend.models.Event;
import com.almatap.AlmatapBackend.models.Image;
import com.almatap.AlmatapBackend.services.EventService;
import com.almatap.AlmatapBackend.services.ImageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/adminPage")
public class AdminController {
    private final EventService eventService;
    private final ImageService imageService;
    private List<String> category;

    public AdminController(EventService eventService, ImageService imageService) {
        this.eventService = eventService;
        this.imageService = imageService;
    }

    @GetMapping()
    public String adminPage(Model model, @ModelAttribute("event") Event event) {

        model.addAttribute("categories", getCategory());
        return "admin/adminPage";
    }

    @PostMapping()
    public String addEvent(@ModelAttribute("event") @Valid Event event,
                           BindingResult bindingResult,
                           @RequestParam("file1") MultipartFile file1,
                           @RequestParam("file2") MultipartFile file2,
                           @RequestParam("file3") MultipartFile file3) throws IOException {

        System.out.println("Binding: " + bindingResult);
        if (bindingResult.hasErrors()){
            return "redirect:/adminPage?error";
        }

        if (file1.getSize() != 0){
            Image image = new Image();
            image.setImage(file1.getBytes());
            image.setSize(file1.getSize());
            event.addImage(image);
        }

        if (file2.getSize() != 0){
            Image image = new Image();
            image.setImage(file2.getBytes());
            image.setSize(file2.getSize());
            event.addImage(image);
        }

        if (file3.getSize() != 0){
            Image image = new Image();
            image.setImage(file3.getBytes());
            image.setSize(file3.getSize());
            event.addImage(image);
        }
        eventService.addEvent(event);
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
                              @ModelAttribute("event") Event event,
                              @RequestParam("file1") MultipartFile file1,
                              @RequestParam("file2") MultipartFile file2,
                              @RequestParam("file3") MultipartFile file3) throws IOException {

        eventService.eventUpdate(file1, file2, file3, id, event);
        return "redirect:/mainPage";
    }

    //delete
    @GetMapping("/delete")
    public String deleteAllEvents(Model model) {
        model.addAttribute("events", eventService.findAllEvent());
        return "admin/deleteAll";
    }

    @GetMapping("/delete/{id}")
    public String deleteEvent(Model model, @PathVariable int id) {
        Event event = eventService.findOne(id);
        model.addAttribute("event", event);
        model.addAttribute("images", event.getImages());
        return "admin/deleteOne";
    }

    @PostMapping("/delete/{id}")
    public String deleteEventPost(@PathVariable int id) {
        eventService.deleteEvent(id);
        return "redirect:/mainPage";
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