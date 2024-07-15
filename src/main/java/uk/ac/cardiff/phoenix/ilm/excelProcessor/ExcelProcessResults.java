package uk.ac.cardiff.phoenix.ilm.excelProcessor;

import lombok.Data;

@Data
public class ExcelProcessResults {
    private ExcelTypeMapping.ExcelDataType dataTypeProcessed;
    private int successRows = 0;
    private int failRows = 0;
    private boolean success = true;

    public ExcelProcessResults(ExcelTypeMapping.ExcelDataType dataTypeProcessed) {
        this.dataTypeProcessed = dataTypeProcessed;
    }

    public void incrementSuccessCount() {
        successRows++;
    }


    public void incrementFailureCount() {
        failRows++;
    }
}
