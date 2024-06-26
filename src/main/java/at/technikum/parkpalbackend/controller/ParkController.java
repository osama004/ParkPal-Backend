package at.technikum.parkpalbackend.controller;

import at.technikum.parkpalbackend.dto.parkdtos.CreateParkDto;
import at.technikum.parkpalbackend.dto.parkdtos.ParkDto;
import at.technikum.parkpalbackend.mapper.ParkMapper;
import at.technikum.parkpalbackend.model.Park;
import at.technikum.parkpalbackend.service.EventService;
import at.technikum.parkpalbackend.service.ParkService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parks")
@CrossOrigin

//@AllArgsConstructor
public class ParkController {
    private final ParkService parkService;

    private final EventService eventService;

    private final ParkMapper parkMapper;

    public ParkController(ParkService parkService, EventService eventService,
                          ParkMapper parkMapper){
        this.parkService = parkService;
        this.eventService = eventService;
        this.parkMapper = parkMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateParkDto createPark(@RequestBody @Valid CreateParkDto createParkDto){
        Park park = parkMapper.createParkDtoToEntity(createParkDto);
        park = parkService.save(park);
        return parkMapper.toCreateParkDto(park);
    }


    @GetMapping
    public List<ParkDto> getAllParks() {
        List<Park> allParks = parkService.findAllParks();
        return allParks.stream().map(park -> parkMapper.toDto(park)).toList();
    }

    @GetMapping("/{parkId}")
    public ParkDto getParkByParkId(@PathVariable @Valid String parkId){
        Park park = parkService.findParkByParkId(parkId);
        return parkMapper.toDto(park);
    }

    @PatchMapping("/update/{parkId}")
    public ParkDto updatePark(@PathVariable String parkId,
                              @RequestBody @Valid ParkDto updatedParkDto){
        Park updatedPark = parkMapper.toEntity(updatedParkDto);
        updatedPark = parkService.updatePark(parkId, updatedPark);
        return parkMapper.toDto(updatedPark);
    }

    @DeleteMapping("/{parkId}")
    //@Preauthorize with Spring security later
    @ResponseStatus(HttpStatus.OK)
    public ParkDto deleteParkByParkById(@PathVariable @Valid String parkId){
        parkService.deleteParkByParkId(parkId);
        return null;
    }

}
