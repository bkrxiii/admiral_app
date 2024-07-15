package uk.ac.cardiff.phoenix.ilm.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.cardiff.phoenix.ilm.excelProcessor.ExcelProcessResults;
import uk.ac.cardiff.phoenix.ilm.model.UploadedFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public interface UploadedFileService {

    void saveUploadedFile(MultipartFile file) throws IOException;


    void deleteUploadedFile(long id) throws FileNotFoundException;

    Optional<UploadedFile> getUploadedFileById(long id);

    List<UploadedFile> getAllUploadedFiles();

    ExcelProcessResults processUploadedFile(long id) throws IOException;

    long getUploadedFileCount();


}