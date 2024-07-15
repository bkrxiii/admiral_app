package uk.ac.cardiff.phoenix.ilm.serviceImpl;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import uk.ac.cardiff.phoenix.ilm.model.Candidate;
import uk.ac.cardiff.phoenix.ilm.model.UploadedFile;
import uk.ac.cardiff.phoenix.ilm.programs.model.Level;
import uk.ac.cardiff.phoenix.ilm.repository.CandidateRepo;
import uk.ac.cardiff.phoenix.ilm.repository.UploadedFileRepo;
import uk.ac.cardiff.phoenix.ilm.service.CandidateReportService;
import uk.ac.cardiff.phoenix.ilm.service.CandidateService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CandidateReportServiceImpl implements CandidateReportService {

    private final CandidateService candidateService;
    private final CandidateRepo candidateRepo;
    private final UploadedFileRepo uploadedFileRepo;

    public CandidateReportServiceImpl(CandidateService candidateService, CandidateRepo candidateRepo, UploadedFileRepo uploadedFileRepo) {
        this.candidateService = candidateService;
        this.candidateRepo = candidateRepo;
        this.uploadedFileRepo = uploadedFileRepo;
    }

    @Override
    public void reportUnregisteredCandidates() {
        candidateService.findCandidatesWithRegistrationDateWithoutRegistrationNumber();
    }

    @Override
    public List<Candidate> reportUnregisteredCandidatesApproachingDeadline() {
        candidateService.findUnregisteredCandidatesOverOneWeekOld();
        return null;
    }

    @Override
    public void reportRegisteredCandidates() throws IOException {
        List<Candidate> candidates = candidateRepo.findByRegistrationNumberIsNotNull();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Registered Candidates");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("First Name");
        headerRow.createCell(2).setCellValue("Last Name");
        headerRow.createCell(3).setCellValue("Date of Birth");
        headerRow.createCell(4).setCellValue("Payroll Number");
        headerRow.createCell(5).setCellValue("Department");
        headerRow.createCell(6).setCellValue("Role");
        headerRow.createCell(7).setCellValue("Registration Date");
        headerRow.createCell(8).setCellValue("Registration Number");
        headerRow.createCell(9).setCellValue("Email");
        headerRow.createCell(10).setCellValue("Level");

        int rowNum = 1;
        for (Candidate candidate : candidates) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(candidate.getId());
            row.createCell(1).setCellValue(candidate.getFirstName());
            row.createCell(2).setCellValue(candidate.getLastName());
            row.createCell(3).setCellValue(candidate.getDateOfBirth().toString());
            row.createCell(4).setCellValue(candidate.getPayrollNumber());
            row.createCell(5).setCellValue(candidate.getDepartment());
            row.createCell(6).setCellValue(candidate.getRole());
            row.createCell(7).setCellValue(candidate.getRegistrationDate() != null ? candidate.getRegistrationDate().toString() : "");
            row.createCell(8).setCellValue(candidate.getRegistrationNumber() != null ? candidate.getRegistrationNumber().toString() : "");
            row.createCell(9).setCellValue(candidate.getEmail());
            row.createCell(10).setCellValue(candidate.getLevel() != null ? candidate.getLevel().toString() : "");
        }

        byte[] workbookBytes = workbookToBytes(workbook);

        LocalDateTime uploadDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
        String timestamp = uploadDateTime.format(formatter);

        String fileName = "registered_candidates_report_" + timestamp + ".xlsx";

        // Create and save UploadedFile entity
        UploadedFile uploadedFile = new UploadedFile(fileName);
        uploadedFile.setContent(workbookBytes);

        uploadedFileRepo.save(uploadedFile);

    }



    @Override
    public void reportRegisteredCandidates(Level level, LocalDate start, LocalDate end) throws IOException {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        List<Candidate> candidates = candidateRepo.findByLevelAndRegistrationDateBetween(level,start, end);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Registered Candidates");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("First Name");
        headerRow.createCell(2).setCellValue("Last Name");
        headerRow.createCell(3).setCellValue("Date of Birth");
        headerRow.createCell(4).setCellValue("Payroll Number");
        headerRow.createCell(5).setCellValue("Department");
        headerRow.createCell(6).setCellValue("Role");
        headerRow.createCell(7).setCellValue("Registration Date");
        headerRow.createCell(8).setCellValue("Registration Number");
        headerRow.createCell(9).setCellValue("Email");
        headerRow.createCell(10).setCellValue("Level");

        int rowNum = 1;
        for (Candidate candidate : candidates) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(candidate.getId());
            row.createCell(1).setCellValue(candidate.getFirstName());
            row.createCell(2).setCellValue(candidate.getLastName());
            row.createCell(3).setCellValue(candidate.getDateOfBirth().toString());
            row.createCell(4).setCellValue(candidate.getPayrollNumber());
            row.createCell(5).setCellValue(candidate.getDepartment());
            row.createCell(6).setCellValue(candidate.getRole());
            row.createCell(7).setCellValue(candidate.getRegistrationDate() != null ? candidate.getRegistrationDate().toString() : "");
            row.createCell(8).setCellValue(candidate.getRegistrationNumber() != null ? candidate.getRegistrationNumber().toString() : "");
            row.createCell(9).setCellValue(candidate.getEmail());
            row.createCell(10).setCellValue(candidate.getLevel() != null ? candidate.getLevel().toString() : "");
        }

        byte[] workbookBytes = workbookToBytes(workbook);

        LocalDateTime uploadDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
        String timestamp = uploadDateTime.format(formatter);

        String fileName = "candidate_ranged_level_report" + timestamp + ".xlsx";

        // Create and save UploadedFile entity
        UploadedFile uploadedFile = new UploadedFile(fileName);
        uploadedFile.setContent(workbookBytes);

        uploadedFileRepo.save(uploadedFile);

    }


    private byte[] workbookToBytes(Workbook workbook) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}
