package uk.ac.cardiff.phoenix.ilm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import uk.ac.cardiff.phoenix.ilm.model.Candidate;
import uk.ac.cardiff.phoenix.ilm.programs.model.Level;
import uk.ac.cardiff.phoenix.ilm.programs.model.Workshop;
import uk.ac.cardiff.phoenix.ilm.service.CandidateService;
import uk.ac.cardiff.phoenix.ilm.programs.service.LevelsService;
import uk.ac.cardiff.phoenix.ilm.programs.service.WorkshopsService;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DatabaseTests {

    @Autowired
    private CandidateService candidateService;
    @Autowired
    private LevelsService levelsService;

    @Autowired
    private WorkshopsService workshopsService;

    @AutoConfigureTestDatabase
    @Test
    @DirtiesContext
    void testSqlInjectionFails(){
        Candidate testCandidate = new Candidate();
        testCandidate.setFirstName("'; DROP TABLE candidate; --\\");
        testCandidate.setLastName("BadActor");
        testCandidate.setEmail("BadActor@gmail.com");
        candidateService.saveCandidate(testCandidate);
        Candidate checkCandidate = candidateService.findCandidateByEmail("BadActor@gmail.com");
        assertEquals(testCandidate.getFirstName(), checkCandidate.getFirstName());
    }

    @AutoConfigureTestDatabase
    @Test
    @DirtiesContext
    void testSqlInjectionFails2() {
        Level level = new Level();
        level.setLevelName("'; DROP TABLE level; --\\");
        level.setLevelDescription("BadActor");
        level.setLevelNumber(1);
        levelsService.addLevel(level);
        Level checkLevel = levelsService.getLevelByInteger(1);
        assertEquals(level.getLevelName(), checkLevel.getLevelName());
    }

    @AutoConfigureTestDatabase
    @Test
    @DirtiesContext
    void testSqlInjectionFails3() {
        Level level = new Level();
        level.setLevelNumber(1);
        level.setLevelName("Level 1");
        level.setLevelDescription("Level 1");
        levelsService.addLevel(level);
        Workshop workshop = new Workshop();
        workshop.setWorkshopName("'; DROP TABLE workshop; --\\");
        workshop.setWorkshopDescription("BadActor");
        workshop.setComponentOf(level);
        workshopsService.addWorkshop(workshop);
        Set<Workshop> workShopSet = workshopsService.getWorkshopsByLevel(level);
        Workshop checkWorkshop = workShopSet.iterator().next();
        assertEquals(workshop.getWorkshopName(), checkWorkshop.getWorkshopName());
    }
}
