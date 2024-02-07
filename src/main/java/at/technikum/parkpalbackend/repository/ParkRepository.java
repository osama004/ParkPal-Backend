package at.technikum.parkpalbackend.repository;

import at.technikum.parkpalbackend.model.Park;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParkRepository extends JpaRepository<Park, String> {
    Optional<Park> findParkByParkId(String parkId);

    Optional<Park>findParkByEventId(String eventId);

    Optional<Park>updatePark(String parkId, Park park);
}