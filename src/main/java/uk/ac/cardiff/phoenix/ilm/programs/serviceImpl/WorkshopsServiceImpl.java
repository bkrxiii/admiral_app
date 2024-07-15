package uk.ac.cardiff.phoenix.ilm.programs.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import uk.ac.cardiff.phoenix.ilm.programs.model.Level;
import uk.ac.cardiff.phoenix.ilm.programs.model.Workshop;
import uk.ac.cardiff.phoenix.ilm.programs.repository.LevelsRepository;
import uk.ac.cardiff.phoenix.ilm.programs.repository.WorkshopsRepository;
import uk.ac.cardiff.phoenix.ilm.programs.service.WorkshopsService;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class WorkshopsServiceImpl implements WorkshopsService {

    Logger logger = LoggerFactory.getLogger(WorkshopsServiceImpl.class);
    private final WorkshopsRepository workshopsRepository;
    private final LevelsRepository levelsRepository;

    public WorkshopsServiceImpl(WorkshopsRepository workshopsRepository, LevelsRepository levelsRepository) {
        this.workshopsRepository = workshopsRepository;
        this.levelsRepository = levelsRepository;
    }

    // constructor by object
    @Override
    public void addWorkshop(Workshop workshop) {
        // Check if workshop already exists and is a component of the same level, as some workshops may be repeated across levels
        Workshop existingWorkshop = workshopsRepository.findWorkshopByWorkshopName(workshop.getWorkshopName());
        if (existingWorkshop != null && Objects.equals(existingWorkshop.getWorkshopName(), workshop.getWorkshopName()) && Objects.equals(existingWorkshop.getComponentOf(), workshop.getComponentOf())) {
            logger.info("ADD Workshop UNSUCCESSFUL. Already exists. Workshop: " + workshop);
            throw new DataIntegrityViolationException("Workshop already exists for this level");
        } else {
            workshopsRepository.save(workshop);
            logger.info("ADD Workshop SUCCESS. Workshop: " + workshop);
        }
    }
    @Override
    public void addWorkshop(String workshopName, String workshopDescription, boolean compulsory, Level componentOf) {
        // Check if workshop already exists and is a component of the same level, as some workshops may be repeated across levels
        Workshop existingWorkshop = workshopsRepository.findWorkshopByWorkshopName(workshopName);
        if (existingWorkshop != null && Objects.equals(existingWorkshop.getWorkshopName(), workshopName) && Objects.equals(existingWorkshop.getComponentOf(), componentOf)) {
            logger.info("ADD Workshop UNSUCCESSFUL. Already exists. Workshop: " + workshopName);
            throw new DataIntegrityViolationException("Workshop already exists for this level");
        } else {
            Workshop newWorkshop = new Workshop(workshopName, workshopDescription, compulsory, componentOf);
            workshopsRepository.save(newWorkshop);
            logger.info("ADD Workshop SUCCESS. Workshop: " + workshopName);
        }
    }

    @Override
    public Set<Workshop> getWorkshopsByLevel(Level level) {
        logger.info("Workshop SEARCH. Level: " + level);
        return workshopsRepository.findByComponentOf(level);
    }

    @Override
    public List<Workshop> getWorkshops() {
        logger.info("Workshop SEARCH ALL.");
        return workshopsRepository.findAll();
    }

    @Override
    public Workshop getWorkshopById(Long id) {
        logger.info("Workshop SEARCH. Workshop ID: " + id);
        return workshopsRepository.findById(id).orElseThrow();
    }

    @Override
    public Set<Workshop> getWorkshopsByLevelNumber(int levelNumber) {
        logger.info("Workshop SEARCH. Level number: " + levelNumber);
        return workshopsRepository.findWorkshopByComponentOf_LevelNumber(levelNumber);
    }
}
