package uk.ac.cardiff.phoenix.ilm.excelProcessor;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ExcelTypeMapping {
    private static final Map<ExcelDataType, List<String>> typeHeadersMap = new HashMap<>();


    static {
        typeHeadersMap.put(ExcelDataType.CANDIDATE, Arrays.asList("firstName", "firstName", "dateOfBirth", "payrollNumber", "department", "role", "registrationDate", "registrationNumber", "email"));
    }

    static List<String> getHeadersForType(ExcelDataType type) {
        return typeHeadersMap.get(type);
    }

    public enum ExcelDataType {
        CANDIDATE, MODULES, LEVELS, SCHEDULE, ATTENDANCE, VOID
    }

}


