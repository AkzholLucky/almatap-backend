package com.almatap.AlmatapBackend.services;

import com.almatap.AlmatapBackend.models.Event;
import com.almatap.AlmatapBackend.repositories.EventRepository;
import org.modelmapper.ModelMapper;
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

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional
    public void addEvent(MultipartFile file, String name, String description, String category) throws IOException {
        Event event = new Event();
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

    public List<Event> findAllEvent() {
        return eventRepository.findAll();
    }

    public Event findOne(int id) {
        return eventRepository.findById(id).orElse(null);
    }
}
