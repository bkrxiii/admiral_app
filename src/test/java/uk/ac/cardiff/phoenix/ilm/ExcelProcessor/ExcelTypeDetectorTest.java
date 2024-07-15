package uk.ac.cardiff.phoenix.ilm.ExcelProcessor;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import uk.ac.cardiff.phoenix.ilm.excelProcessor.ExcelTypeDetector;
import uk.ac.cardiff.phoenix.ilm.excelProcessor.ExcelTypeMapping;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExcelTypeDetectorTest {

    @Test
    void testDetectExcelTypeWithCandidateData() throws IOException, InvalidFormatException {
        File file = new ClassPathResource("candidatetemplate.xlsx").getFile();
        Workbook workbook = new XSSFWorkbook(file);
        ExcelTypeDetector detector = new ExcelTypeDetector();
        assertEquals(ExcelTypeMapping.ExcelDataType.CANDIDATE, detector.detectExcelType(workbook));
    }

    @Test
    void  testDetectUnknownExcelType() throws IOException, InvalidFormatException {
        File file = new ClassPathResource("invalidtempalte.xlsx").getFile();
        Workbook workbook = new XSSFWorkbook(file);
        ExcelTypeDetector detector = new ExcelTypeDetector();
        assertEquals(ExcelTypeMapping.ExcelDataType.VOID, detector.detectExcelType(workbook));
    }
}