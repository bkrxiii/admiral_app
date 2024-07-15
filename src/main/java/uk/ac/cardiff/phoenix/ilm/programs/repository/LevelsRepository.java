package uk.ac.cardiff.phoenix.ilm.programs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.cardiff.phoenix.ilm.programs.model.Level;


@Repository
public interface LevelsRepository extends JpaRepository<Level, Long> {
    // added in case we need to find by name or number
    // not implemented yet, may be deleted in the future if not necessary
    Level findByLevelName(String levelName);
    // implemented
    Level findByLevelNumber(int levelNumber);
    // add a level to the database
}
