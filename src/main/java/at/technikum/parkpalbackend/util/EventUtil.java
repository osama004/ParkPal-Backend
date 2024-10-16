package at.technikum.parkpalbackend.util;

import at.technikum.parkpalbackend.dto.eventdtos.CreateEventDto;
import at.technikum.parkpalbackend.dto.eventdtos.EventDto;
import at.technikum.parkpalbackend.exception.EntityNotFoundException;
import at.technikum.parkpalbackend.mapper.EventMapper;
import at.technikum.parkpalbackend.model.*;
import at.technikum.parkpalbackend.persistence.EventRepository;
import at.technikum.parkpalbackend.persistence.ParkRepository;
import at.technikum.parkpalbackend.service.EventTagService;
import at.technikum.parkpalbackend.service.FileService;
import at.technikum.parkpalbackend.service.ParkService;
import at.technikum.parkpalbackend.service.EventService;
import at.technikum.parkpalbackend.service.UserService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EventUtil {

    private final EventRepository eventRepository;
    private final FileService fileService;
    private final EventMapper eventMapper;
    private final EventTagService eventTagService;
    private final ParkService parkService;
    private final UserService userService;
    private final EventService eventService;
    private final ParkRepository parkRepository;

    public EventUtil(EventRepository eventRepository,
                     FileService fileService,
                     EventMapper eventMapper,
                     EventTagService eventTagService,
                     ParkService parkService,
                     UserService userService,
                     EventService eventService,
                     ParkRepository parkRepository) {
        this.eventRepository = eventRepository;
        this.fileService = fileService;
        this.eventMapper = eventMapper;
        this.eventTagService = eventTagService;
        this.parkService = parkService;
        this.userService = userService;
        this.eventService = eventService;
        this.parkRepository = parkRepository;
    }

    public CreateEventDto saveCreateEvent(CreateEventDto createEventDto) {

        User creator = userService.findByUserId(createEventDto.getCreatorUserId());
        Park park = parkService.findParkById(createEventDto.getParkId());
        List<File> mediaFiles = fileService.getFileList(createEventDto.getMediaFileExternalIds());
        Set<EventTag> eventTags = eventTagService.findTagsByIds(createEventDto.getEventTagsIds());

        Event event = eventMapper.toEntityCreateEvent(
                createEventDto, creator, park, mediaFiles, eventTags);

        event = eventService.save(event);

        addEventToTags(eventTags, event);
        fileService.setEventMedia(event, mediaFiles);

        return eventMapper.toDtoCreateEvent(event);
    }

    public Event updateEvent(String eventId, EventDto eventDto) {
        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Event not found with id: " + eventId));

        // Call separate functions for each update step
        updateBasicEventDetails(existingEvent, eventDto);
        updateEventPark(existingEvent, eventDto);
        updateEventUsers(existingEvent, eventDto);
        updateEventTags(existingEvent, eventDto);
        updateEventMedia(existingEvent, eventDto);

        eventService.validateEventTimes(existingEvent.getStartTS(), existingEvent.getEndTS());
        return eventService.save(existingEvent);
    }

    private void updateBasicEventDetails(Event event, EventDto eventDto) {
        event.setTitle(eventDto.getTitle());
        event.setDescription(eventDto.getDescription());
        event.setStartTS(eventDto.getStartTS());
        event.setEndTS(eventDto.getEndTS());
    }

    private void updateEventPark(Event event, EventDto eventDto) {
        if (eventDto.getParkId() != null) {
            Optional<Park> optionalPark = parkRepository.findById(eventDto.getParkId());
            if (optionalPark.isPresent()) {
                Park associatedPark = optionalPark.get();
                event.setPark(associatedPark);
            }
        }
    }

    private void updateEventUsers(Event event, EventDto eventDto) {
        if (eventDto.getJoinedUserIds() != null) {

            List<User> newJoinedUsers = eventDto.getJoinedUserIds().stream()
                    .map(userService::findByUserId)
                    .toList();

            // Remove the event from old users' joined events list
            for (User oldUser : event.getJoinedUsers()) {
                oldUser.getJoinedEvents().remove(event);
            }

            // Clear the existing joined users from the event
            event.getJoinedUsers().clear();

            // Set the bidirectional relationship for new users
            for (User newUser : newJoinedUsers) {
                event.getJoinedUsers().add(newUser);
                newUser.getJoinedEvents().add(event);
            }
        }
    }

    private void updateEventTags(Event event, EventDto eventDto) {
        if (eventDto.getEventTagsIds() != null) {
            Set<EventTag> eventTags = eventDto.getEventTagsIds().stream()
                    .map(eventTagService::findTagById)
                    .collect(Collectors.toSet());

            // Clear the existing tags to avoid duplicates
            event.getTags().clear();

            // Maintain the bidirectional relationship
            for (EventTag tag : eventTags) {
                tag.getEvents().add(event);
                event.getTags().add(tag);
            }
        }
    }

    private void updateEventMedia(Event event, EventDto eventDto) {
        for (File oldMedia : event.getMedia()) {
            oldMedia.setEvent(null);
        }
        event.getMedia().clear();

        if (eventDto.getMediaFileExternalIds() != null
                && !eventDto.getMediaFileExternalIds().isEmpty()) {
            List<File> mediaFiles = eventDto.getMediaFileExternalIds().stream()
                    .map(fileService::findFileByExternalId)
                    .toList();

            for (File mediaFile : mediaFiles) {
                mediaFile.setEvent(event);
                event.getMedia().add(mediaFile);
            }
        }
    }

    public void addEventToTags(Set<EventTag> tags, Event event) {
        for (EventTag tag : tags) {
            tag.getEvents().add(event);
        }
    }

}
