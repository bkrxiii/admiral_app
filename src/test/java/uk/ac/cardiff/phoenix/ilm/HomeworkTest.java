// Ai was used as an inspiration
package uk.ac.cardiff.phoenix.ilm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.cardiff.phoenix.ilm.model.Candidate;
import uk.ac.cardiff.phoenix.ilm.programs.model.Homework;
import uk.ac.cardiff.phoenix.ilm.programs.model.Level;
import uk.ac.cardiff.phoenix.ilm.programs.model.Workshop;
import uk.ac.cardiff.phoenix.ilm.programs.repository.HomeworkRepository;
import uk.ac.cardiff.phoenix.ilm.programs.repository.WorkshopsRepository;
import uk.ac.cardiff.phoenix.ilm.programs.service.LevelsService;
import uk.ac.cardiff.phoenix.ilm.repository.CandidateRepo;
import uk.ac.cardiff.phoenix.ilm.service.CandidateService;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class HomeworkTest {

    // This is the candidate for whom we want to generate homework objects for
    private Candidate testCandidate;
    // load candidate repository
//    CandidateRepo candidateRepository;
//    WorkshopsRepository workshopService;

    @Autowired
    private CandidateRepo candidateRepository;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private LevelsService levelsService;

    @Autowired
    private WorkshopsRepository workshopsRepository;

    @Autowired
    private HomeworkRepository homeworkRepository;

    // dependency injection
//    public HomeworkTest(CandidateRepo candidateRepository, WorkshopsRepository workshopService) {
//        this.candidateRepository = candidateRepository;
//        this.workshopService = workshopService;
//    }
    // set testCandidate to the candidate with id 1
    @BeforeEach
    public void setUp() {
        testCandidate = candidateRepository.findCandidateById(1L);
    }
    @Test
    public void testGenerateHomeworkForCandidate() {
        // debug
        System.out.println("Candidate: " + testCandidate);
        // assert
        assertNotNull(testCandidate, "Candidate with id 1 not found");

        // enroll candidate to level to 3 (id 1)
        candidateService.enrollCandidateInLevel(testCandidate, levelsService.getLevelById(1L));

        // Get the level of the candidate
        Level candidateLevel = testCandidate.getLevel();
        // debug
        System.out.println("Candidate level: " + candidateLevel);
        // assert
        assertNotNull(candidateLevel, "Candidate level is null");
        assertEquals("Level 3", candidateLevel.getLevelName(), "Candidate level is not Level 3");

        // extract Long from candidateLevel
        Long candidateLevelId = candidateLevel.getId();
        // debug
        System.out.println("Candidate level id: " + candidateLevelId);
        // assert
        assertEquals(1L, candidateLevelId, "Candidate level id is not 1");

        // Get all workshops for the candidate's level
        Set<Workshop> workshopsForLevel = workshopsRepository.findWorkshopByComponentOf_LevelId(candidateLevelId);

        // Create Homework instances for each workshop
        Set<Homework> homeworkSet = new HashSet<>();
        for (Workshop workshop : workshopsForLevel) {
            Homework homework = new Homework(workshop, testCandidate.getId(), false);
            // save homework to generate ids
            homeworkRepository.save(homework);
            homeworkSet.add(homework);
            // debug
            System.out.println("Homework: " + homework);
        }
        // assert
        assertEquals(6, homeworkSet.size(), "Homework set size is not 6");
    }

}
