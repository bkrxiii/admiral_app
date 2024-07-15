package uk.ac.cardiff.phoenix.ilm.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.cardiff.phoenix.ilm.programs.model.Workshop;
import uk.ac.cardiff.phoenix.ilm.programs.service.LevelsService;
import uk.ac.cardiff.phoenix.ilm.programs.service.WorkshopsService;


import java.util.List;
import java.util.Set;

@Controller
public class LevelsController {

    private final WorkshopsService workshopsService;

    public LevelsController(WorkshopsService workshopsService) {
        this.workshopsService = workshopsService;
    }

    // show specific level by number (I.E. level 3 = /levels/3)
    @GetMapping("/levels/{id}")
    public ModelAndView getLevelByInteger(@PathVariable ("id") int levelNumber) {
        ModelAndView modelAndView = new ModelAndView("levels/levelsList");
        Set<Workshop> workshops = workshopsService.getWorkshopsByLevelNumber(levelNumber);
        modelAndView.addObject("workshops", workshops);
        modelAndView.addObject("title_insert", "Level " + levelNumber);
        return modelAndView;
    }

    // show all workshops on a page
    @GetMapping("/workshops")
    public ModelAndView getWorkshops() {
        ModelAndView modelAndView = new ModelAndView("workshops/workshopsList");
        List<Workshop> workshops = workshopsService.getWorkshops();
        modelAndView.addObject("workshops", workshops);
        return modelAndView;
    }

    // show specific workshop by id
    @GetMapping("/workshops/{id}")
    public ModelAndView getWorkshopsById(@PathVariable ("id") Long workshopId) {
        ModelAndView modelAndView = new ModelAndView("workshops/workshopsList");
        Workshop workshop = workshopsService.getWorkshopById(workshopId);
        modelAndView.addObject("workshops", workshop);
        modelAndView.addObject("title_insert", workshop.getWorkshopName());
        return modelAndView;
    }


}
