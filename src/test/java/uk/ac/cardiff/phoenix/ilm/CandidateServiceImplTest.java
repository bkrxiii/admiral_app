// AI was used as an influence

package uk.ac.cardiff.phoenix.ilm;

import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.cardiff.phoenix.ilm.model.Candidate;
import uk.ac.cardiff.phoenix.ilm.programs.model.Level;
import uk.ac.cardiff.phoenix.ilm.service.CandidateService;

import static java.sql.Types.NULL;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase
@SpringBootTest
public class CandidateServiceImplTest {

    @Autowired
    private CandidateService candidateService; // create to test

    // test that ensures candidate is saved
    @Test
    @Transactional
    public void testSaveCandidate() {
        long countBefore = candidateService.findAll().size();
//        System.out.println("Number of candidates before: " + countBefore);
        Candidate testCandidate = new Candidate();
        testCandidate.setFirstName("Zach3713");
        testCandidate.setLastName("Zachary3713");
        testCandidate.setEmail("zachzacharythethird3713@aol.com");
        testCandidate = candidateService.saveCandidate(testCandidate);
        long countAfter = candidateService.findAll().size();
//        System.out.println("Number of candidates after: " + countAfter);
        assertEquals(countBefore + 1, countAfter, "Candidate not saved.");
    }

    // test that confirms all candidates that are saved can be looked up by email
    @Test
    @Transactional
    public void testFindCandidateByEmail() {
        // try to find all available candidates by emails
        for (Candidate candidate : candidateService.findAll()) {
            Candidate testCandidate = candidateService.findCandidateByEmail(candidate.getEmail());
//            System.out.println("Candidate found by email: " + testCandidate.getEmail());
            assertEquals(candidate.getEmail(), testCandidate.getEmail(), "Candidate not found by email.");
        }
    }

    // test that confirms all candidates that are saved can be looked up by ID
    @Test
    @Transactional
    public void testFindCandidateById() {
        // try to find all available candidates by emails
        for (Candidate candidate : candidateService.findAll()) {
            Candidate testCandidate = candidateService.findCandidateById(candidate.getId());
//            System.out.println("Candidate found by ID: " + testCandidate.getId());
            assertEquals(candidate.getId(), testCandidate.getId(), "Candidate not found by ID.");
        }
    }


    // test that ID is generated - this is also my code?
    // looks like my work from 3/12 got overwritten...? SHA: 700429e6a87b8d5febdaed9ddc9ca588808f8513 or https://git.cardiff.ac.uk/c22123521/year2_team_phoenix/-/blob/69f23ae50a64479554f8e1a8c5694db82c890ed4/src/test/java/uk/ac/cardiff/phoenix/ilm/CandidateServiceImplTest.java
    @Test
    @Transactional
    void testIdGeneratedCandidate() {
        Candidate testCandidate = new Candidate();
        testCandidate = candidateService.saveCandidate(testCandidate);
        assertTrue(testCandidate.getId() != NULL, "ID is NULL.");
    }

    // test that ID is unchanged

    // this is my code though, why does it say only Dan?
    @Test
    @Transactional // I remember writing this to delete the data after the test is run.
    void testIdUnchangedCandidate() {
        Candidate testCandidate3 = new Candidate();
        testCandidate3.setEmail("test0110@aol.com"); // and I use aol email for testing
        testCandidate3 = candidateService.saveCandidate(testCandidate3);

        final Candidate finalTestCandidate = testCandidate3;

        // Attempt to save the candidate with the same ID
        assertThrows(EntityExistsException.class, () -> candidateService.saveCandidate(finalTestCandidate));
    }

//     test that can find by email

//    @Test
//    @DirtiesContext
//    void testFindCandidateByEmail() {
//        Candidate testCandidate4 = new Candidate();
//        testCandidate4.setFirstName("John"); // same here?
//        testCandidate4.setLastName("Wick");
//        testCandidate4.setEmail("babayaga@aol.com");
//        testCandidate4 = candidateService.saveCandidate(testCandidate4);
//        Candidate testCandidate5 = candidateService.findCandidateByEmail("babayaga@aol.com");
//        assertEquals(testCandidate4.getFirstName(), testCandidate5.getFirstName(), "Cannot find candidate by email.");
//    }


    // this is correct, this one I didn't write.
    @Test
    @Transactional
    void testEnrollCandidateInLevel() {
        Candidate testCandidate = new Candidate();
        testCandidate.setEmail("enrollme@gmail.com");

        Level testLevel = new Level();
        testLevel.setLevelId(1L);
        testLevel.setLevelNumber(1);
        testLevel.setLevelName("Level 1");
        testLevel.setLevelDescription("Level 1 Description");

        // Enroll the candidate in the level
        Candidate enrolledCandidate = candidateService.enrollCandidateInLevel(testCandidate, testLevel);

        assertNotNull(enrolledCandidate.getLevel(), "Candidate is not enrolled in the expected level.");
    }


    // this one I didn't write, that's correct.
    @Test
    @Transactional
    void testEnrollAlreadyEnrolledCandidateInLevel() {
        Candidate testCandidate = new Candidate();
        testCandidate.setEmail("alreadyenrolled@gmail.com");

        Level testLevel = new Level();
        testLevel.setLevelId(1L);
        testLevel.setLevelNumber(1);
        testLevel.setLevelName("Level 1");
        testLevel.setLevelDescription("Level 1 Description");

        // Enroll the candidate in the first level
        candidateService.enrollCandidateInLevel(testCandidate, testLevel);

        // Attempt to enroll the candidate in the second level (should throw an exception)
        assertThrows(EntityExistsException.class, () -> candidateService.enrollCandidateInLevel(testCandidate, testLevel));
    }

    // correct
    @Test
    @Transactional
    void testILMRegistration() {
        Candidate testCandidate4 = new Candidate();
        testCandidate4.setFirstName("John");
        testCandidate4.setLastName("Wick");
        testCandidate4.setEmail("babayaga@aol.com");
        testCandidate4 = candidateService.saveCandidate(testCandidate4);
        candidateService.registerILM(testCandidate4, 123456789L);
        assertEquals(testCandidate4.getRegistrationNumber(), 123456789L, "ILM not registered.");
    }

    // correct
    @Test
    @Transactional
    void testILMRegistrationThrowsErrorIfAlreadyRegistered() {
        Candidate testCandidate4 = new Candidate();
        testCandidate4.setFirstName("John");
        testCandidate4.setLastName("Wick");
        testCandidate4.setEmail("babayaga@aol.com");
        testCandidate4 = candidateService.saveCandidate(testCandidate4);
        candidateService.registerILM(testCandidate4, 123456789L);
        Candidate finalTestCandidate = testCandidate4;
        assertThrows(EntityExistsException.class, () -> candidateService.registerILM(finalTestCandidate, 123456789L));
}}

