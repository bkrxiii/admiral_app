// Ai was used as an influence

package uk.ac.cardiff.phoenix.ilm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.cardiff.phoenix.ilm.model.Candidate;
import uk.ac.cardiff.phoenix.ilm.programs.model.Homework;
import uk.ac.cardiff.phoenix.ilm.programs.model.Workshop;
import uk.ac.cardiff.phoenix.ilm.programs.repository.HomeworkRepository;
import uk.ac.cardiff.phoenix.ilm.programs.repository.WorkshopsRepository;
import uk.ac.cardiff.phoenix.ilm.programs.service.HomeworkService;
import uk.ac.cardiff.phoenix.ilm.programs.service.LevelsService;
import uk.ac.cardiff.phoenix.ilm.programs.service.WorkshopsService;
import uk.ac.cardiff.phoenix.ilm.service.CandidateService;

import java.util.List;
import java.util.Set;

@Controller
public class LookupController {
    private CandidateService candidateService;
    private LevelsService levelsService;
    @Autowired
    private HomeworkService homeworkService;
    @Autowired
    private HomeworkRepository homeworkRepository;
    @Autowired
    private WorkshopsService workshopsService;
    @Autowired
    private WorkshopsRepository workshopsRepository;

    // I thought @Autowired injects the dependencies automatically?
    public LookupController(CandidateService candidateService, LevelsService levelsService, HomeworkService homeworkService, HomeworkRepository homeworkRepository, WorkshopsService workshopsService, WorkshopsRepository workshopsRepository) {
        this.candidateService = candidateService;
        this.levelsService = levelsService;
        this.homeworkService = homeworkService;
        this.homeworkRepository = homeworkRepository;
        this.workshopsService = workshopsService;
        this.workshopsRepository = workshopsRepository;
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

        // add the candidate's homework to the model
        // I know it call the repository directly, but it's the only way I could get it to work -- hope I can get some feedback on this to understand where I went wrong.
        List<Homework> homeworkList = homeworkRepository.findAllHomeworksByCandidateId(id);
        modelAndView.addObject("homeworkList", homeworkList);

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
            Set<Workshop> workshopsForLevel = workshopsRepository.findWorkshopByComponentOf_LevelId(Long.parseLong(levelId));
            candidateService.enrollCandidateInLevel(candidateService.findCandidateById(Long.parseLong(candidateId)), levelsService.getLevelById(Long.parseLong(levelId)));
            // on submission, create homework objects for each workshop in the selected level
            for (Workshop workshop : workshopsForLevel) {
                Homework homework = new Homework(workshop, Long.parseLong(candidateId), false);
                homeworkRepository.save(homework);
                // debug
                System.out.println("Homework created: " + homework);
            }
            // print success message
            redirectAttributes.addFlashAttribute("message", "Successfully enrolled candidate in level");
        } catch (Exception e) {
            // print failure message
            redirectAttributes.addFlashAttribute("message", "Failed to enroll candidate in level");
        }
        // need to create homework objects for each workshop in the selected level



        return "redirect:/lookup/" + candidateId;
    }

    @PostMapping("/lookup/update/")
    public String updateHomework(
            @RequestParam("homeworkId") Long homeworkId,
            @RequestParam("candidateId") Long candidateId,
            @RequestParam("completed") Boolean completed,
            RedirectAttributes redirectAttributes)
    {
        // debug
        // System.out.println("Homework ID: " + homeworkId);
        // System.out.println("Candidate ID: " + candidateId);
        // System.out.println("Completed: " + completed);
        // again using repository, as for some reason service didn't work.
        Homework homeworkToChange = homeworkRepository.getHomeworkById(homeworkId);
        // debug
        // System.out.println("Homework to change: " + homeworkToChange);

        try {
            // get the boolean
            Boolean completedStatus = homeworkToChange.getCompleted();
            // debug
            // System.out.println("Homework completed: " + completedStatus);

             // set the boolean to the opposite of what it is
            homeworkToChange.setCompleted(!completedStatus);
            // debug
            // System.out.println("Homework statues now: " + homeworkToChange);

            // save the homework object
            homeworkRepository.save(homeworkToChange);


        } catch (Exception e) {
            // print failure message
            redirectAttributes.addFlashAttribute("message", "Failed to update homework");
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
