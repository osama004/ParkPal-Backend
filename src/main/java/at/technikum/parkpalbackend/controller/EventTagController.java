package at.technikum.parkpalbackend.controller;

import at.technikum.parkpalbackend.dto.EventTagDto;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController

@CrossOrigin



public class EventTagController {
    @GetMapping
    public List<EventTagDto> getAll() {
        List<EventTagDto> eventTags = new ArrayList<>();

        return eventTags;
    } //WIP copied from Osama; needs to have more functionality
}
