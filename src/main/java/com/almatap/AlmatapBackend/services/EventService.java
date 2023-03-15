package com.almatap.AlmatapBackend.services;

import com.almatap.AlmatapBackend.models.Event;
import com.almatap.AlmatapBackend.models.Image;
import com.almatap.AlmatapBackend.repositories.EventRepository;
import com.almatap.AlmatapBackend.repositories.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final RatingService ratingService;
    private final ImageRepository imageRepository;

    public EventService(EventRepository eventRepository, RatingService ratingService, ImageRepository imageRepository) {
        this.eventRepository = eventRepository;
        this.ratingService = ratingService;
        this.imageRepository = imageRepository;
    }

    @Transactional
    public void addEvent(Event event) {
        eventRepository.save(event);
    }

    public List<Event> findAllWithRatingFilter(double rating, int min, int max) {
        return findAllEvent()
                .stream()
                .filter(event -> event.getAverageRating() >= rating && event.getPrice() >= min && event.getPrice() <= max)
                .toList();
    }

    public List<Event> findAllEvent(){
        List<Event> allEvents = eventRepository.findAll();
        allEvents.forEach(event -> event.setAverageRating(ratingService.averageRating(event)));

        return allEvents;
    }

    public Event findOne(int id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Transactional
    public void eventUpdate(MultipartFile file1, MultipartFile file2, MultipartFile file3, int id, Event event) throws IOException {
        Event updatedEvent = eventRepository.findById(id).orElse(null);
        assert updatedEvent != null;

        updatedEvent.setDescription(event.getDescription());
        updatedEvent.setCategory(event.getCategory());
        updatedEvent.setName(event.getName());

        imageRepository.deleteByEventId(id);

        if (file1.getSize() != 0){
            Image image = new Image();
            image.setImage(file1.getBytes());
            image.setSize(file1.getSize());
            updatedEvent.addImage(image);
        }

        if (file2.getSize() != 0){
            Image image = new Image();
            image.setImage(file2.getBytes());
            image.setSize(file2.getSize());
            updatedEvent.addImage(image);
        }

        if (file3.getSize() != 0){
            Image image = new Image();
            image.setImage(file3.getBytes());
            image.setSize(file3.getSize());
            updatedEvent.addImage(image);
        }

        eventRepository.save(updatedEvent);
    }

    public List<Event> findByNameStartsWith(String name){
        List<Event> allEvents = eventRepository.findByNameStartingWith(name);

        for (Event event : allEvents){
            double averageRating = ratingService.averageRating(event);
            event.setAverageRating(averageRating);
        }

        return allEvents;
    }

    @Transactional
    public void deleteEvent(int id){
        eventRepository.deleteById(id);
    }
}
