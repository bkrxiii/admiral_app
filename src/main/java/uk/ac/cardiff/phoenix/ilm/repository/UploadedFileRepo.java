package uk.ac.cardiff.phoenix.ilm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.cardiff.phoenix.ilm.model.UploadedFile;

import java.util.Optional;

public interface UploadedFileRepo extends JpaRepository<UploadedFile, Long> {

    Optional<UploadedFile> findById(long id);
}

