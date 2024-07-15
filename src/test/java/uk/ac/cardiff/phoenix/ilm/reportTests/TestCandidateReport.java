package uk.ac.cardiff.phoenix.ilm.reportTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import uk.ac.cardiff.phoenix.ilm.repository.UploadedFileRepo;
import uk.ac.cardiff.phoenix.ilm.serviceImpl.CandidateReportServiceImpl;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureTestDatabase
@SpringBootTest
public class TestCandidateReport {
    @Autowired
    CandidateReportServiceImpl candidateReportService;
    @Autowired
    UploadedFileRepo uploadedFileRepo;

    @Test
    @DirtiesContext
    public void testCandidateReport() throws IOException {
        int uploadedFileCountBefore = uploadedFileRepo.findAll().size();
        candidateReportService.reportRegisteredCandidates();
        int uploadedFileCountAfter = uploadedFileRepo.findAll().size();
        assertEquals(uploadedFileCountAfter - uploadedFileCountBefore, 1);
    }






}
