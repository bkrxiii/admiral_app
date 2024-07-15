package uk.ac.cardiff.phoenix.ilm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import uk.ac.cardiff.phoenix.ilm.programs.model.Level;
import uk.ac.cardiff.phoenix.ilm.programs.model.Workshop;
import uk.ac.cardiff.phoenix.ilm.programs.service.LevelsService;
import uk.ac.cardiff.phoenix.ilm.programs.service.WorkshopsService;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {
    private final LevelsService levelsService;
    private final WorkshopsService workshopsService;

    public GlobalControllerAdvice(LevelsService levelsService, WorkshopsService workshopsService) {
        this.levelsService = levelsService;
        this.workshopsService = workshopsService;
    }

    @ModelAttribute("levelsList")
    public List<Level> getLevels() {
        return levelsService.getLevels();
    }

    @ModelAttribute("workshopsList")
    public List<Workshop> getWorkshops() {
            return workshopsService.getWorkshops();
    }

}
