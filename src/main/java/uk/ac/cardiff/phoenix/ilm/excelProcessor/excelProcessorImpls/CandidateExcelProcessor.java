package uk.ac.cardiff.phoenix.ilm.excelProcessor.excelProcessorImpls;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.ac.cardiff.phoenix.ilm.excelProcessor.ExcelProcessResults;
import uk.ac.cardiff.phoenix.ilm.excelProcessor.ExcelProcessor;
import uk.ac.cardiff.phoenix.ilm.excelProcessor.ExcelProcessorFactory;
import uk.ac.cardiff.phoenix.ilm.excelProcessor.ExcelTypeMapping;
import uk.ac.cardiff.phoenix.ilm.model.Candidate;
import uk.ac.cardiff.phoenix.ilm.service.CandidateService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component
public class CandidateExcelProcessor implements ExcelProcessor {

    CandidateService candidateService;
    ExcelProcessResults excelProcessResults = new ExcelProcessResults(ExcelTypeMapping.ExcelDataType.CANDIDATE);




    public CandidateExcelProcessor(CandidateService candidateService) {
        this.candidateService = candidateService;
    }


    @Override
    public ExcelProcessResults process(Workbook workbook) {
        List<Candidate> candidates = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(0);

        // Assuming your data starts from the second row (index 1)
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                // Skip the first row with headings
                continue;
            }
            try {
                // Assuming the columns you want to extract data from are at indexes 0, 1, and 2
                Cell cell0 = row.getCell(0); // firstName
                Cell cell1 = row.getCell(1); // lastName
                Cell cell2 = row.getCell(2); // dateOfBirth
                Cell cell3 = row.getCell(3); // payrollNumber
                Cell cell4 = row.getCell(4); // department
                Cell cell5 = row.getCell(5); // role
                Cell cell6 = row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK); // registrationDate
                Cell cell7 = row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK); // registrationNumber
                Cell cell8 = row.getCell(8); // email

                // Extract data from the cells
                String firstName = cell0.getStringCellValue();
                String lastName = cell1.getStringCellValue();
                LocalDate dateOfBirth = cell2.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                long payrollNumber = (long) cell3.getNumericCellValue();
                String department = cell4.getStringCellValue();
                String role = cell5.getStringCellValue();
                LocalDate registrationDate = cell6.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                long registrationNumber = (long) cell7.getNumericCellValue();
                String email = cell8.getStringCellValue();

                Candidate candidate = new Candidate();
                candidate.setFirstName(firstName);
                candidate.setLastName(lastName);
                candidate.setDateOfBirth(dateOfBirth);
                candidate.setPayrollNumber(payrollNumber);
                candidate.setDepartment(department);
                candidate.setRole(role);
                candidate.setRegistrationDate(registrationDate);
                candidate.setRegistrationNumber(registrationNumber);
                candidate.setEmail(email);

                candidates.add(candidate);

            } catch (Exception e) {
                // Handle exception
                // do nothing with data
            }
        }
        for (Candidate candidate : candidates) {
            try {
                candidateService.saveCandidate(candidate);
                excelProcessResults.incrementSuccessCount();
            } catch (Exception e) {
                excelProcessResults.incrementFailureCount();
            }
        }


        return excelProcessResults; // Indicate that processing is complete
    }

}