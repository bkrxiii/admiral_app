// Ai was used as an influence

package uk.ac.cardiff.phoenix.ilm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.cardiff.phoenix.ilm.model.Candidate;
import uk.ac.cardiff.phoenix.ilm.service.CandidateService;
import java.time.LocalDate;
import java.util.List;




@Controller
public class SubmitController {
    private CandidateService candidateService;

    public SubmitController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }


    @GetMapping("/submission/")
    public ModelAndView getSubmission() {
        List<Candidate> candidates = candidateService.findAll();

        ModelAndView modelAndView = new ModelAndView("submission");
        modelAndView.addObject("title_insert", "Submission");
        modelAndView.addObject("candidates", candidates);
        return modelAndView;
    }

    @PostMapping("/submission/")
    public String postSubmission(@ModelAttribute Candidate candidate, RedirectAttributes redirectAttributes) {
        // if candidate email doesn't contain @, then return error
        if (!candidate.getEmail().contains("@admiral")) {
            redirectAttributes.addFlashAttribute("message", "Email address not valid Admiral email.");
            return "redirect:/submission/";
        } else if (candidateService.existsCandidateByEmail(candidate.getEmail())) {
            redirectAttributes.addFlashAttribute("message", "Candidate with email " + "<" + candidate.getEmail() + ">" + " already exists");
            return "redirect:/submission/";
            // if candidate date of birth makes them over 100, then return error
        } else if (candidate.getDateOfBirth().getYear() < LocalDate.now().getYear() - 100) {
            redirectAttributes.addFlashAttribute("message", "Candidate date of birth makes them over 100 years old.");
            return "redirect:/submission/";
            // if candidate date of birth makes them under 16, then return error
        }  else if (candidate.getDateOfBirth().isAfter(LocalDate.now())) {
            redirectAttributes.addFlashAttribute("message", "Candidate date of birth is in the future.");
            return "redirect:/submission/";
            // if candidate date of birth makes them over 100, then return error
        } else if (candidate.getDateOfBirth().getYear() > LocalDate.now().getYear() - 16) {
            redirectAttributes.addFlashAttribute("message", "Candidate date of birth makes them under 18 years old.");
            return "redirect:/submission/";
            // if candidate payroll number is not 6 digits, then return error
        } else if (String.valueOf((candidate.getPayrollNumber())).length() != 5){
            redirectAttributes.addFlashAttribute("message", "Candidate payroll number is not 5 digits.");
            return "redirect:/submission/";
        } else {
            try {
                candidateService.saveCandidate(candidate);
                System.out.println(candidate.toString());
                redirectAttributes.addFlashAttribute("message", "Successfully added new candidate to database.");
                return "redirect:/lookup/" + candidate.getId(); // can add level straight away here after
            } catch (Exception e) {
                System.out.println("Failed to save candidate");
                redirectAttributes.addFlashAttribute("message", "Unable to add candidate to database.");
                return "redirect:/submission/";
            }
        }
    }
}
