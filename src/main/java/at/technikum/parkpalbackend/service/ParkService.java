package at.technikum.parkpalbackend.service;

import at.technikum.parkpalbackend.model.Park;
import at.technikum.parkpalbackend.repository.ParkRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkService {
    private final ParkRepository parkRepository;

    public ParkService(ParkRepository parkRepository) {
        this.parkRepository = parkRepository;
    }

    public List<Park> findAllParks() {
        return parkRepository.findAll();
    }
    public Park findParkByParkId(String parkId) {
        return parkRepository.findParkByParkId(parkId).orElseThrow();
    }

    public Park findParkByEventId(String eventId){
        return parkRepository.findParkByEventId(eventId).orElseThrow();
    }


    public Park save(Park park) {
        return parkRepository.save(park);
    }

    public Park updatePark(String parkId, Park updatedPark) {
        Park park = parkRepository.findParkByParkId(parkId).orElseThrow();

        park.setParkId(updatedPark.getParkId());
        park.setParkName(updatedPark.getParkName());
        park.setDescription(updatedPark.getDescription());
        park.setParkAddress(updatedPark.getParkAddress());

        return parkRepository.save(park);
    }

    public Park deleteParkByParkId(String parkId) {
        Park park = parkRepository.findParkByParkId(parkId).orElseThrow();
        parkRepository.delete(park);
        return null;
    }
}
