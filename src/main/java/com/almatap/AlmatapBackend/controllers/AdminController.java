package com.almatap.AlmatapBackend.controllers;

import com.almatap.AlmatapBackend.models.Event;
import com.almatap.AlmatapBackend.models.Image;
import com.almatap.AlmatapBackend.services.EventService;
import com.almatap.AlmatapBackend.services.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/adminPage")
public class AdminController {
    private final EventService eventService;
    private final UsersService usersService;
    private List<String> category;

    public static List<String> notifications = new ArrayList<>();

    public AdminController(EventService eventService, UsersService usersService) {
        this.eventService = eventService;
        this.usersService = usersService;
    }

    @GetMapping()
    public Map<String, Object> adminPage(@ModelAttribute("event") Event event) {

        Map<String, Object> map = new HashMap<>();
        map.put("categories", getCategory());
        return map;
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> addEvent(@ModelAttribute("event") @Valid Event event,
                                               BindingResult bindingResult,
                                               @RequestParam("file1") String file1,
                                               @RequestParam("file2") String file2,
                                               @RequestParam("file3") String file3) {


        if (bindingResult.hasErrors()){
            return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
        }

        if (!file1.isEmpty()){

            Image image1 = new Image();
            image1.setImage(file1);
            event.addImage(image1);
        }

        if (!file2.isEmpty()){

            Image image2 = new Image();
            image2.setImage(file2);
            event.addImage(image2);
        }

        if (!file3.isEmpty()){

            Image image3 = new Image();
            image3.setImage(file3);
            event.addImage(image3);
        }

        usersService.getAll();
        eventService.addEvent(event);

        Event event1 = eventService.findEventByName(event.getName());

        String notification = String.format(
                "Welcome to almatap. Please, visit next link to activate your account: https://almatap-backend.onrender.com/event/%s",
                event1.getId()
        );
        notifications.add(notification);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //update
    @GetMapping("/change")
    public Map<String, Object> changeAllEvents() {

        Map<String, Object> map = new HashMap<>();

        map.put("events", eventService.findAllEvent());
        return map;
    }

    @GetMapping("/change/{id}")
    public Map<String, Object> changeEvent(@PathVariable int id) {

        Map<String, Object> map = new HashMap<>();
        map.put("event", eventService.findOne(id));
        return map;
    }

    @PostMapping("/change/{id}")
    public ResponseEntity<HttpStatus> saveChanges(@PathVariable int id,
                              @ModelAttribute("event") Event event,
                              @RequestParam("file1") String file1,
                              @RequestParam("file2") String file2,
                              @RequestParam("file3") String file3) {

        eventService.eventUpdate(file1, file2, file3, id, event);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //delete
    @GetMapping("/delete")
    public Map<String, Object> deleteAllEvents() {
        Map<String, Object> map = new HashMap<>();
        map.put("events", eventService.findAllEvent());
        return map;
    }

    @GetMapping("/delete/{id}")
    public Map<String, Object> deleteEvent(@PathVariable int id) {
        Map<String, Object> map = new HashMap<>();
        Event event = eventService.findOne(id);

        map.put("event", event);
        return map;
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteEventPost(@PathVariable int id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private List<String> getCategory(){

        category = new ArrayList<>();

        category.add("Parks");
        category.add("Shopping centre");
        category.add("Entertainment center");
        category.add("Mountains");

        return category;
    }

//    private Event addImageToEvent(String file, Event event){
//        if (!file.isEmpty()){
//
//            Image image1 = new Image();
//            image1.setImage(file);
//            event.addImage(image1);
//            return event;
//        }
//
//    }
}