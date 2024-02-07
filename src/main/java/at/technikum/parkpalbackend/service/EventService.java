package at.technikum.parkpalbackend.service;

import at.technikum.parkpalbackend.exception.EntityNotFoundException;
import at.technikum.parkpalbackend.model.Event;
import at.technikum.parkpalbackend.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public List<Event> findAllEvents() {
         return eventRepository.findAll();
    }

    public Event findByEventId(String eventId) {
        return eventRepository.findByEventId(eventId).orElseThrow(EntityNotFoundException::new);
    }

    public List<Event> findAllEventsByPark(String parkId) {
        return eventRepository.findAllByPark_ParkId(parkId);
    }

    public List<Event> findAllEventsByUser(String userId) {
        List<Event> events = eventRepository.findAllByCreator_UserId(userId);
        // check if even the user exists
        // if the User has not created any Event ??!!
        return events;
    }
}