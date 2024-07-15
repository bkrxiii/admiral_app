package uk.ac.cardiff.phoenix.ilm.excelProcessor;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import uk.ac.cardiff.phoenix.ilm.excelProcessor.excelProcessorImpls.CandidateExcelProcessor;
import uk.ac.cardiff.phoenix.ilm.service.CandidateService;

@Component
public class TypedExcelProcessorFactory implements ExcelProcessorFactory{

    private final CandidateService candidateService;


    public TypedExcelProcessorFactory(CandidateService candidateService) {
        this.candidateService = candidateService;
    }
    @Override
    public ExcelProcessor createExcelProcessor (Workbook workbook) throws IllegalArgumentException {
        ExcelTypeMapping.ExcelDataType type = new ExcelTypeDetector().detectExcelType(workbook);
        return switch (type) {
            case CANDIDATE -> new CandidateExcelProcessor(candidateService);
            default -> throw new IllegalArgumentException("Invalid Excel file type");
        };
    }
}
