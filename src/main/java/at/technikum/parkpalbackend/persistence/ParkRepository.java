package at.technikum.parkpalbackend.persistence;

import at.technikum.parkpalbackend.model.Park;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParkRepository extends JpaRepository<Park, String> {
    boolean existsByName(String name);
    Optional<Park> findByName(String name);
}
