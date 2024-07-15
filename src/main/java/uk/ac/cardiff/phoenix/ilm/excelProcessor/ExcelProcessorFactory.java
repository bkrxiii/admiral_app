package uk.ac.cardiff.phoenix.ilm.excelProcessor;

import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelProcessorFactory{
    public ExcelProcessor createExcelProcessor(Workbook workbook) throws IllegalArgumentException;
}
