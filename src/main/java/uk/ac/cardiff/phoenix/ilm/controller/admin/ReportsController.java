package uk.ac.cardiff.phoenix.ilm.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.cardiff.phoenix.ilm.programs.model.Level;
import uk.ac.cardiff.phoenix.ilm.programs.service.LevelsService;
import uk.ac.cardiff.phoenix.ilm.service.CandidateReportService;

import java.time.LocalDate;

@RequestMapping("/admin/reports")
@Controller
public class ReportsController {
    CandidateReportService candidateReportService;
    LevelsService levelsService;

    public ReportsController(CandidateReportService candidateReportService, LevelsService levelsService) {
        this.candidateReportService = candidateReportService;
        this.levelsService = levelsService;
    }


    @GetMapping()
    public ModelAndView ReportLandingPage() {
        ModelAndView modelAndView = new ModelAndView("admin/reports");
        modelAndView.addObject("title_insert", "Reports");
        return modelAndView;
    }

    @PostMapping("/simpleactivecandidates")
    public String SimpleActiveCandidatesReport(RedirectAttributes redirectAttributes) {
        try {
            candidateReportService.reportRegisteredCandidates();
            redirectAttributes.addFlashAttribute("message", "Report processed successfully.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error - report couldn't be processed.");
        }
        return "redirect:/admin/reports";
    }

    @PostMapping("/candidatereport")
    public String CandidateReport(
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr,
            @RequestParam("levelId") Long levelId,
            RedirectAttributes redirectAttributes) {
        try {
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);
            Level level = levelsService.getLevelById(levelId);
            candidateReportService.reportRegisteredCandidates(level,startDate, endDate);
            redirectAttributes.addFlashAttribute("message", "Report processed successfully.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error - report couldn't be processed.");
        }
        return "redirect:/admin/reports";
    }


}
