package uk.ac.cardiff.phoenix.ilm.excelProcessor;

import org.apache.poi.ss.usermodel.*;

import java.util.Iterator;
import java.util.List;

public class ExcelTypeDetector {
    public ExcelTypeMapping.ExcelDataType detectExcelType(Workbook workbook) {
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(0);

        for (ExcelTypeMapping.ExcelDataType type : ExcelTypeMapping.ExcelDataType.values()) {
            List<String> expectedHeaders = ExcelTypeMapping.getHeadersForType(type);
            if (expectedHeaders == null) {
                continue;
            }
            if (matchesHeaders(row, expectedHeaders)) {
                return type;
            }

        }
        return ExcelTypeMapping.ExcelDataType.VOID;
    }

    public static boolean matchesHeaders(Row row, List<String> expectedHeaders) {
        Iterator<Cell> cellIterator = row.cellIterator();
        for (String s : expectedHeaders) {
            if (!cellIterator.hasNext()) {
                return false; // Not enough cells in the row
            }

            Cell cell = cellIterator.next();
            String cellValue = cell.getStringCellValue();

            if (!cellValue.equals(s)) {
                return false; // Header does not match
            }
        }
        // if there is another cell in the row, then the header does not match
        return !cellIterator.hasNext();
    }
}



