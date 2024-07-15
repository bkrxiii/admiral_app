// Ai was used as an influence

package uk.ac.cardiff.phoenix.ilm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.cardiff.phoenix.ilm.controller.LookupController;
import uk.ac.cardiff.phoenix.ilm.model.Candidate;
import uk.ac.cardiff.phoenix.ilm.programs.model.Level;
import uk.ac.cardiff.phoenix.ilm.programs.service.LevelsService;
import uk.ac.cardiff.phoenix.ilm.service.CandidateService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestLookupController {

    @Mock
    private CandidateService candidateService;

    @Mock
    private LevelsService levelsService;

    @InjectMocks
    private LookupController lookupController;

    @Test
    public void testLookupCandidate() {
        List<Candidate> mockCandidates = Collections.singletonList(new Candidate());
        when(candidateService.findAll()).thenReturn(mockCandidates);

        ModelAndView modelAndView = lookupController.lookupCandidate();

        assertEquals("lookup", modelAndView.getViewName());
        assertEquals("lookup", modelAndView.getModel().get("title_insert"));
        assertEquals(mockCandidates, modelAndView.getModel().get("candidates"));
    }

    @Test
    public void testLookupCandidateById() {
        Long candidateId = 1L;
        Candidate mockCandidate = new Candidate();
        when(candidateService.findCandidateById(candidateId)).thenReturn(mockCandidate);

        ModelAndView modelAndView = lookupController.lookupCandidateById(candidateId);

        assertEquals("candidate", modelAndView.getViewName());
        assertEquals("Candidate", modelAndView.getModel().get("title_insert"));
        assertEquals(mockCandidate, modelAndView.getModel().get("candidate"));
    }

    @Test
    public void testLookupCandidateByDepartment() {
        String department = "IT";
        List<Candidate> mockCandidates = Collections.singletonList(new Candidate());
        when(candidateService.findCandidateByDepartment(department)).thenReturn(mockCandidates);

        ModelAndView modelAndView = lookupController.lookupCandidateByDepartment(department);

        assertEquals("lookup", modelAndView.getViewName());
        assertEquals("lookup", modelAndView.getModel().get("title_insert"));
        assertEquals(mockCandidates, modelAndView.getModel().get("candidates"));
    }

    @Test
    public void testEnrollCandidate() {
        Long candidateId = 1L;
        Long levelId = 3L;
        when(candidateService.findCandidateById(candidateId)).thenReturn(new Candidate());
        when(levelsService.getLevelById(levelId)).thenReturn(new Level());

        String result = lookupController.enrollCandidate(levelId.toString(), candidateId.toString(), Mockito.mock(RedirectAttributes.class));

        assertEquals("redirect:/lookup/" + candidateId, result);
        verify(candidateService).enrollCandidateInLevel(any(), any());
    }

    @Test
    public void testRegisterILM() {
        Long candidateId = 1L;
        Long ilmNumber = 12388L;
        when(candidateService.findCandidateById(candidateId)).thenReturn(new Candidate());

        String result = lookupController.registerILM(candidateId, ilmNumber, Mockito.mock(RedirectAttributes.class));

        assertEquals("redirect:/lookup/" + candidateId, result);
        verify(candidateService).registerILM(any(), anyLong());
    }
}
