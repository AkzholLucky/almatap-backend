package com.almatap.AlmatapBackend.services;

import com.almatap.AlmatapBackend.models.Event;
import com.almatap.AlmatapBackend.models.Favorite;
import com.almatap.AlmatapBackend.models.Image;
import com.almatap.AlmatapBackend.models.User;
import com.almatap.AlmatapBackend.repositories.EventRepository;
import com.almatap.AlmatapBackend.repositories.FavoriteRepository;
import com.almatap.AlmatapBackend.repositories.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final RatingService ratingService;
    private final ImageRepository imageRepository;
    private final FavoriteRepository favoriteRepository;

    public EventService(EventRepository eventRepository, RatingService ratingService, ImageRepository imageRepository, FavoriteRepository favoriteRepository) {
        this.eventRepository = eventRepository;
        this.ratingService = ratingService;
        this.imageRepository = imageRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public void addEvent(Event event) {
        eventRepository.save(event);
    }

    public List<Event> findAllWithRatingFilter(double rating, int min, int max, String city, String category) {

//        return
        return city.equals("default") ?
                findAllEvent()
                .stream()
                .filter(event -> event.getAverageRating() >= rating && event.getPrice() >= min && event.getPrice() <= max && event.getCategory().equals(category)).toList()
                :
                findAllEvent()
                .stream()
                .filter(event -> event.getAverageRating() >= rating && event.getPrice() >= min && event.getPrice() <= max && event.getCity().equals(city) && event.getCategory().equals(category))
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

    public Event findEventByName(String name){
        return eventRepository.findByName(name);
    }

    @Transactional
    public void eventUpdate(String file1, String file2, String file3, int id, Event event) {
        Event updatedEvent = eventRepository.findById(id).orElse(null);
        assert updatedEvent != null;

        updatedEvent.setDescription(event.getDescription());
        updatedEvent.setCategory(event.getCategory());
        updatedEvent.setName(event.getName());

        imageRepository.deleteByEventId(id);

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

            System.out.println(file3);
            Image image3 = new Image();
            image3.setImage(file3);
            event.addImage(image3);
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
    public void addOrDeleteFavorites(User user, Event event){
        Optional<Favorite> favorite = getAllFavorites().stream()
                .filter(fav -> fav.getUser().getId() == user.getId() && fav.getEvent().getId() == event.getId())
                .findAny();

        if (favorite.isPresent()){
            favoriteRepository.delete(favorite.get());

        } else {
            Favorite newFavorite = new Favorite();
            newFavorite.setUser(user);
            newFavorite.setEvent(event);
            favoriteRepository.save(newFavorite);
        }
    }

    public List<Favorite> getAllFavorites(){
        return favoriteRepository.findAll();
    }

    public List<Favorite> getAllFavoritesOfUser(int userId){
        return favoriteRepository.findAll().stream()
                .filter(f -> f.getUser().getId() == userId).toList();
    }

    @Transactional
    public void deleteEvent(int id){
        eventRepository.deleteById(id);
    }
}
