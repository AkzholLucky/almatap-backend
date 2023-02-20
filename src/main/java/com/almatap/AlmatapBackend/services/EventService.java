package com.almatap.AlmatapBackend.services;

import com.almatap.AlmatapBackend.models.Event;
import com.almatap.AlmatapBackend.repositories.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final RatingService ratingService;

    public EventService(EventRepository eventRepository, RatingService ratingService) {
        this.eventRepository = eventRepository;
        this.ratingService = ratingService;
    }

    @Transactional
    public void addEvent(MultipartFile file, String name, String description, String category) throws IOException {
        Event event = new Event();
        eventInfo(file, name, description, category, event);
    }

    public List<Event> findAllEvent() {
        List<Event> allEvents = eventRepository.findAll();

        for (Event event : allEvents){
            double averageRating = ratingService.averageRating(event);
            event.setAverageRating(averageRating);
        }
        return allEvents;
    }

    public Event findOne(int id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Transactional
    public void eventUpdate(MultipartFile file, String name, String description, String category, int id) throws IOException {
        Event event = eventRepository.findById(id).orElse(null);

        eventInfo(file, name, description, category, event);
    }

    @Transactional
    public void deleteEvent(int id){
        eventRepository.deleteById(id);
    }

    private void eventInfo(MultipartFile file, String name, String description, String category, Event event) throws IOException {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        if (fileName.contains("..")) {
            System.out.println("not a valid file");
        }

        event.setImage(Base64.getEncoder().encodeToString(file.getBytes()));

        event.setDescription(description);

        event.setName(name);

        event.setCategory(category);

        eventRepository.save(event);
    }
}
