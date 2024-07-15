package uk.ac.cardiff.phoenix.ilm.programs.serviceImpl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import uk.ac.cardiff.phoenix.ilm.programs.model.Level;
import uk.ac.cardiff.phoenix.ilm.programs.repository.LevelsRepository;
import uk.ac.cardiff.phoenix.ilm.programs.service.LevelsService;

import java.util.List;

@Service
public class LevelsServiceImpl implements LevelsService {

    Logger logger = LoggerFactory.getLogger(LevelsServiceImpl.class);
    private final LevelsRepository levelsRepository;

    public LevelsServiceImpl(LevelsRepository levelsRepository) {
        this.levelsRepository = levelsRepository;
    }

    // get all levels
    @Override
    public List<Level> getLevels() {
        logger.info("Levels SEARCH ALL.");
        return levelsRepository.findAll();
    }

    // constructor by object, check if level already exists by level number. Return error if it does.
    @Override
    public void addLevel(Level level) {
        try {
            levelsRepository.save(level);
            logger.info("Level SAVED. Level: " + level);
        } catch (DataIntegrityViolationException e) {
            logger.info("Level SAVE UNSUCCESSFUL. Already exists. Level: " + level);
            throw new DataIntegrityViolationException("Level " + level.getLevelNumber() + " already exists");
        }
    }

    // get level by number
    @Override
    public Level getLevelByInteger(int levelNumber) {
        logger.info("Level SEARCH. Level number: " + levelNumber);
        return levelsRepository.findByLevelNumber(levelNumber);
    }

    @Override
    public Level getLevelById(Long id) {
        logger.info("Level SEARCH. Level ID: " + id);
        return levelsRepository.findById(id).orElseThrow();
    }

    // constructor by fields, check if level already exists by level number. Return error if it does.
    @Override
    public void addLevel(String levelName, String levelDescription, int levelNumber) {
        // Check if level already exists
        try {
            levelsRepository.save(new Level(levelName, levelDescription, levelNumber));
            logger.info("New Level SAVED. Level: " + levelNumber);
        } catch (DataIntegrityViolationException e) {
            logger.info("New Level SAVE UNSUCCESSFUL. Already exists. Level: " + levelNumber);
            throw new DataIntegrityViolationException("Level " + levelNumber + " already exists");
        }
    }
}
