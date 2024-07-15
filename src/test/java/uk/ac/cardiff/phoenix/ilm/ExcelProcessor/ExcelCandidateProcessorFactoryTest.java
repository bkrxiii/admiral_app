package uk.ac.cardiff.phoenix.ilm.ExcelProcessor;

import jakarta.persistence.EntityExistsException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.core.io.ClassPathResource;
import uk.ac.cardiff.phoenix.ilm.excelProcessor.ExcelProcessResults;
import uk.ac.cardiff.phoenix.ilm.excelProcessor.excelProcessorImpls.CandidateExcelProcessor;
import uk.ac.cardiff.phoenix.ilm.model.Candidate;
import uk.ac.cardiff.phoenix.ilm.service.CandidateService;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;

@AutoConfigureTestDatabase
public class ExcelCandidateProcessorFactoryTest {

    @Mock
    private CandidateService candidateService;


    @InjectMocks
    private CandidateExcelProcessor candidateExcelProcessor;


    @Test
    void testCandidateExcelProcessor() throws IOException, InvalidFormatException {
        MockitoAnnotations.openMocks(this);
        doAnswer((Answer<Candidate>) invocation -> invocation.getArgument(0))
                .when(candidateService).saveCandidate(any(Candidate.class));

        candidateExcelProcessor = new CandidateExcelProcessor(candidateService);
        File file = new ClassPathResource("candidatetemplate.xlsx").getFile();
        Workbook workbook = new XSSFWorkbook(file);

        ExcelProcessResults processResults = candidateExcelProcessor.process(workbook);

        int numberOfRows = workbook.getSheetAt(0).getLastRowNum();

        assertEquals(numberOfRows, processResults.getSuccessRows());

    }

    @Test
    void TestNoRowsAddedOnFail() throws IOException, InvalidFormatException {
        MockitoAnnotations.openMocks(this);
        doThrow(new EntityExistsException("Duplicate entry for candidate"))
                .when(candidateService).saveCandidate(any(Candidate.class));
        candidateExcelProcessor = new CandidateExcelProcessor(candidateService);
        File file = new ClassPathResource("candidatetemplate.xlsx").getFile();
        Workbook workbook = new XSSFWorkbook(file);

        ExcelProcessResults processResults = candidateExcelProcessor.process(workbook);

        assertEquals(0, processResults.getSuccessRows());



    }



}
