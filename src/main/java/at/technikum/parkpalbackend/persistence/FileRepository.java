package at.technikum.parkpalbackend.persistence;

import at.technikum.parkpalbackend.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, String> {

    Optional <File> findByExternalId(String externalId);
}