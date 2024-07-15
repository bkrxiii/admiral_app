package uk.ac.cardiff.phoenix.ilm.ExcelProcessor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import uk.ac.cardiff.phoenix.ilm.model.UploadedFile;
import uk.ac.cardiff.phoenix.ilm.repository.CandidateRepo;
import uk.ac.cardiff.phoenix.ilm.serviceImpl.UploadedFileProcessServiceImpl;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
public class TestUploadedFileService {
    @Autowired
    private UploadedFileProcessServiceImpl uploadedFileProcessService;
    @Autowired
    private CandidateRepo candidateRepo;


    @Test
    @DirtiesContext

    public void testProcessUploadedFile_Successful() throws IOException {
        File file = new ClassPathResource("candidatetemplate.xlsx").getFile();

        int sizeBefore = candidateRepo.findAll().size();

        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.streamFileToByte(file);
        uploadedFile.setFileName("candidatetemplate.xlsx");

        uploadedFileProcessService.processUploadedFile(uploadedFile);
        assertEquals(candidateRepo.findAll().size() - sizeBefore, 2);
    }

}
