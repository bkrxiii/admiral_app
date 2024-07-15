package uk.ac.cardiff.phoenix.ilm.programs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.cardiff.phoenix.ilm.programs.model.Level;
import uk.ac.cardiff.phoenix.ilm.programs.model.Workshop;
import java.util.Set;

@Repository
public interface WorkshopsRepository extends JpaRepository<Workshop, Long> {
    // implemented
    Set<Workshop> findByComponentOf(Level level);
    // implemented
    Workshop findWorkshopByWorkshopName(String workshopName);
    // not implemented. Creates a set of workshops that are part of a level and are compulsory
    Set<Workshop> findByComponentOfAndCompulsory(Level level, boolean compulsory);
    // not implemented. Creates a set of workshops that are compulsory
    Set<Workshop> findByCompulsory(boolean compulsory);
    Set <Workshop> findWorkshopByComponentOf_LevelNumber(int levelNumber);
}
