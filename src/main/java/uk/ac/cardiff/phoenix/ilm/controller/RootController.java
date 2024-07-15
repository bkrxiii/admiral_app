package uk.ac.cardiff.phoenix.ilm.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.cardiff.phoenix.ilm.model.Candidate;
import uk.ac.cardiff.phoenix.ilm.service.CandidateReportService;
import uk.ac.cardiff.phoenix.ilm.service.CandidateService;

import java.util.List;

@Controller
public class RootController {
    CandidateService candidateService;
    RootController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }
        /**
         * This is the root controller for the application.
         * Currently, this is a placeholder for the home page, with some code to demonstrate how to use base templates.
         * It is used to display the home page.
         * @return ModelAndView
         */
        @GetMapping("/")
        public ModelAndView root() {
            ModelAndView modelAndView;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                List<Candidate> candidates = candidateService.findUnregisteredCandidatesOverOneWeekOld();
                modelAndView = new ModelAndView("landing");
                modelAndView.addObject("title_insert", "Dashboard");
                modelAndView.addObject("candidates", candidates);
            }
            else { // not logged in
                modelAndView = new ModelAndView("landingpagenologin");
                modelAndView.addObject("title_insert", "Home");
            }
            return modelAndView;
        }

}
