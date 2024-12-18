package at.technikum.parkpalbackend.util;

import at.technikum.parkpalbackend.dto.parkdtos.CreateParkDto;
import at.technikum.parkpalbackend.dto.parkdtos.ParkDto;
import at.technikum.parkpalbackend.exception.EntityAlreadyExistsException;
import at.technikum.parkpalbackend.exception.EntityNotFoundException;
import at.technikum.parkpalbackend.mapper.ParkMapper;
import at.technikum.parkpalbackend.model.Event;
import at.technikum.parkpalbackend.model.File;
import at.technikum.parkpalbackend.model.Park;
import at.technikum.parkpalbackend.persistence.ParkRepository;
import at.technikum.parkpalbackend.service.EventService;
import at.technikum.parkpalbackend.service.FileService;
import at.technikum.parkpalbackend.service.ParkService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Component
public class ParkUtil {

    private final FileService fileService;
    private final ParkMapper parkMapper;
    private final ParkRepository parkRepository;
    private final EventService eventService;
    private final ParkService parkService;

    public ParkUtil(FileService fileService,
                    ParkMapper parkMapper,
                    ParkRepository parkRepository,
                    EventService eventService, ParkService parkService) {
        this.fileService = fileService;
        this.parkMapper = parkMapper;
        this.parkRepository = parkRepository;
        this.eventService = eventService;
        this.parkService = parkService;
    }

    public CreateParkDto saveCreatePark(CreateParkDto createParkDto) {
        if (parkRepository.existsByName(createParkDto.getName())) {
            throw new EntityAlreadyExistsException("A park with the name " +
                createParkDto.getName() + " already exists.");
        }

        List<File> mediaFiles = fileService.getFileList(createParkDto.getMediaFileExternalIds());
        Park park = parkMapper.createParkDtoToEntity(createParkDto, mediaFiles);

        try {
            // Save the park to the repository
            park = parkService.save(park);
        } catch (DataIntegrityViolationException e) {
            // Handle potential unique constraint violation
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                throw new EntityNotFoundException("A park with this name already exists.");
            }
            throw e;
        }

        fileService.setParkMedia(park, mediaFiles);
        return parkMapper.toCreateParkDto(park);
    }

    //@Transactional
    public Park updatePark(String parkId, ParkDto parkDto) {
        Park existingPark = parkRepository.findById(parkId)
            .orElseThrow(() ->
                new EntityNotFoundException("Park not found with id: " + parkId));
        if (existingPark == null) {
            throw new EntityNotFoundException("Park not found with id " + parkId);
        }
        updateBasicParkDetails(existingPark, parkDto);
        updateParkEvents(existingPark, parkDto);
        updateParkMedia(existingPark, parkDto);

        try {
            // Attempt to save the updated park
            return parkService.save(existingPark);
        } catch (DataIntegrityViolationException e) {
            // Handle potential unique constraint violation
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                throw new EntityAlreadyExistsException(
                    "A park with the name '" + parkDto.getName() + "' already exists.");
            }
            throw e;
        }
    }

    private void updateBasicParkDetails(Park park, ParkDto parkDto) {
        park.setName(parkDto.getName());
        park.setDescription(parkDto.getDescription());
        park.setAddress(parkDto.getAddress());
    }

    public void updateParkEvents(Park park, ParkDto parkDto) {
        if (!parkDto.getEventIds().isEmpty()) {

            List<String> uniqueEventIds = new ArrayList<>(new HashSet<>(parkDto.getEventIds()));

            List<Event> newEvents = uniqueEventIds.stream()
                .map(eventService::findByEventId)
                .filter(Objects::nonNull)
                .toList();

            // Remove the park from old events
            for (Event oldEvent : park.getEvents()) {
                oldEvent.setPark(null);
            }

            // Clear the existing events
            park.getEvents().clear();

            // Add new events and maintain bidirectional relationship
            for (Event newEvent : newEvents) {
                park.getEvents().add(newEvent);
                newEvent.setPark(park);
            }
        }
    }

    void updateParkMedia(Park park, ParkDto parkDto) {
        if (!parkDto.getMediaFileExternalIds().isEmpty()) {
            for (File oldFile : park.getMedia()) {
                oldFile.setPark(null);
            }
            park.getMedia().clear();

            List<File> mediaFiles = parkDto.getMediaFileExternalIds().stream()
                .map(fileService::findFileByExternalId)
                .toList();

            for (File mediaFile : mediaFiles) {
                mediaFile.setPark(park);
                park.getMedia().add(mediaFile);
            }
        }
    }
}
