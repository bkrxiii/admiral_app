package uk.ac.cardiff.phoenix.ilm.service;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.stereotype.Service;
import uk.ac.cardiff.phoenix.ilm.excelProcessor.ExcelProcessResults;
import uk.ac.cardiff.phoenix.ilm.model.UploadedFile;

import java.io.IOException;

@Service
public interface UploadedFileProcessService {
    public ExcelProcessResults processUploadedFile(UploadedFile file) throws IOException;
}
