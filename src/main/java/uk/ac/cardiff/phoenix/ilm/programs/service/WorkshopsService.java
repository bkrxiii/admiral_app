package uk.ac.cardiff.phoenix.ilm.programs.service;

import org.springframework.stereotype.Service;
import uk.ac.cardiff.phoenix.ilm.programs.model.Level;
import uk.ac.cardiff.phoenix.ilm.programs.model.Workshop;

import java.util.List;
import java.util.Set;

@Service
public interface WorkshopsService {
    // constructor by object
    void addWorkshop(Workshop workshop);
    // constructor by fields
    void addWorkshop(String workshopName, String workshopDescription, boolean compulsory, Level componentOf);
    List<Workshop> getWorkshops();

    Set<Workshop> getWorkshopsByLevel(Level level);

    Workshop getWorkshopById(Long id);

    Set<Workshop> getWorkshopsByLevelNumber(int levelNumber);
}
