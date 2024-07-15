package uk.ac.cardiff.phoenix.ilm.programs.service;

import org.springframework.stereotype.Service;
import uk.ac.cardiff.phoenix.ilm.programs.model.Level;
import java.util.List;

@Service
public interface LevelsService {

    // constructor by fields
    void addLevel(String levelName, String levelDescription, int levelNumber);
    List<Level> getLevels();
    // constructor by object
    void addLevel(Level level);
    // get level by number
    Level getLevelByInteger(int levelNumber);

    Level getLevelById(Long id);

}

