// Ai was used as an influence

package uk.ac.cardiff.phoenix.ilm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.cardiff.phoenix.ilm.model.Candidate;
import uk.ac.cardiff.phoenix.ilm.programs.service.LevelsService;
import uk.ac.cardiff.phoenix.ilm.service.CandidateService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;

@Controller
public class LookupController {
    private CandidateService candidateService;
    private LevelsService levelsService;

    public LookupController(CandidateService candidateService, LevelsService levelsService) {
        this.candidateService = candidateService;
        this.levelsService = levelsService;
    }

    @GetMapping("/lookup/")
    public ModelAndView lookupCandidate() {

        ModelAndView modelAndView = new ModelAndView("lookup");
        modelAndView.addObject("title_insert", "lookup");

        List<Candidate> candidates = candidateService.findAll();
        modelAndView.addObject("candidates", candidates);

        return modelAndView;
    }

    @GetMapping("/lookup/{id}")
    public ModelAndView lookupCandidateById(@PathVariable Long id) {

        ModelAndView modelAndView = new ModelAndView("candidate");
        modelAndView.addObject("title_insert", "Candidate");

        Candidate candidate = candidateService.findCandidateById(id);
        modelAndView.addObject("candidate", candidate);

        return modelAndView;
    }

    @GetMapping("/lookup/department/{department}")
    public ModelAndView lookupCandidateByDepartment(@PathVariable String department) {

        ModelAndView modelAndView = new ModelAndView("lookup");
        modelAndView.addObject("title_insert", "lookup");

        List<Candidate> candidates = candidateService.findCandidateByDepartment(department);
        modelAndView.addObject("candidates", candidates);

        return modelAndView;
    }

    @PostMapping("/candidate/enroll")
    public String enrollCandidate(
            @RequestParam("levelId") String levelId,
            @RequestParam("candidateId") String candidateId,
            RedirectAttributes redirectAttributes){
        try {
            candidateService.enrollCandidateInLevel(candidateService.findCandidateById(Long.parseLong(candidateId)), levelsService.getLevelById(Long.parseLong(levelId)));
            redirectAttributes.addFlashAttribute("message", "Successfully enrolled candidate in level");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Failed to enroll candidate in level");
        }

        return "redirect:/lookup/" + candidateId;
    }

    @GetMapping("/candidate/registerILM")
    public String registerILM(
            @RequestParam("candidateId") Long candidateId,
            @RequestParam("ilmNumber") Long ilmNumber,
            RedirectAttributes redirectAttributes) {

        try {
            candidateService.registerILM(candidateService.findCandidateById(candidateId), ilmNumber);
            redirectAttributes.addFlashAttribute("message", "Successfully registered ILM");

        } catch (Exception e) {
            // If an exception occurs during registration, add a failure message
            redirectAttributes.addFlashAttribute("message", "Failed to register ILM");
        }

        // Redirect back to /lookup/{id} with the candidateId
        return "redirect:/lookup/" + candidateId;
    }


}
