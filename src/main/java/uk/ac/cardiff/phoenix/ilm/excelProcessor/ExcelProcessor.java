package uk.ac.cardiff.phoenix.ilm.excelProcessor;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
@Component
public interface ExcelProcessor {

        ExcelProcessResults process(Workbook workbook);

}
