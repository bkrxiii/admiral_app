package uk.ac.cardiff.phoenix.ilm.serviceImpl;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import uk.ac.cardiff.phoenix.ilm.excelProcessor.ExcelProcessResults;
import uk.ac.cardiff.phoenix.ilm.excelProcessor.ExcelProcessor;
import uk.ac.cardiff.phoenix.ilm.excelProcessor.ExcelTypeMapping;
import uk.ac.cardiff.phoenix.ilm.excelProcessor.TypedExcelProcessorFactory;
import uk.ac.cardiff.phoenix.ilm.model.UploadedFile;
import uk.ac.cardiff.phoenix.ilm.service.UploadedFileProcessService;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class UploadedFileProcessServiceImpl implements UploadedFileProcessService {

    TypedExcelProcessorFactory typedExcelProcessorFactory;

    public UploadedFileProcessServiceImpl(TypedExcelProcessorFactory typedExcelProcessorFactory) {
        this.typedExcelProcessorFactory = typedExcelProcessorFactory;
    }

    @Override
    public ExcelProcessResults processUploadedFile(UploadedFile file) throws IOException,IllegalArgumentException {
        ExcelProcessResults excelProcessResults;
        excelProcessResults = new ExcelProcessResults(ExcelTypeMapping.ExcelDataType.VOID);
        excelProcessResults.setSuccess(false);
        Workbook workbook = null;
            // Create a Workbook from the byte array
        try {
            workbook = WorkbookFactory.create(new ByteArrayInputStream(file.getContent()));

            ExcelProcessor excelProcessor = typedExcelProcessorFactory.createExcelProcessor(workbook);
            return excelProcessor.process(workbook);
        }
        finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    // Handle close exception if needed
                    e.printStackTrace();
                }
            }
        }

    }
}
