package uk.ac.cardiff.phoenix.ilm.serviceImpl;

import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.cardiff.phoenix.ilm.excelProcessor.ExcelProcessResults;
import uk.ac.cardiff.phoenix.ilm.model.UploadedFile;
import uk.ac.cardiff.phoenix.ilm.repository.UploadedFileRepo;
import uk.ac.cardiff.phoenix.ilm.service.UploadedFileProcessService;
import uk.ac.cardiff.phoenix.ilm.service.UploadedFileService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UploadedFileServiceImpl implements UploadedFileService {

    Logger logger = LoggerFactory.getLogger(UploadedFileServiceImpl.class);
    private final UploadedFileRepo uploadedFileRepo;
    private final UploadedFileProcessService UploadedFileProcessService;

    public UploadedFileServiceImpl(UploadedFileRepo uploadedFileRepo, uk.ac.cardiff.phoenix.ilm.service.UploadedFileProcessService uploadedFileProcessService) {
        this.uploadedFileRepo = uploadedFileRepo;
        UploadedFileProcessService = uploadedFileProcessService;
    }


    @Override
    @Transactional
    public void saveUploadedFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        UploadedFile uploadedFile = new UploadedFile(fileName);
        try (var inputStream = file.getInputStream()) {
            uploadedFile.streamContentToByte(inputStream);
        }
        uploadedFileRepo.save(uploadedFile);
        logger.info("File SAVED. File name: " + fileName);
    }

    @Override
    public void deleteUploadedFile(long id) throws FileNotFoundException {
        if (uploadedFileRepo.findById(id).isEmpty()) {
            logger.info("File search UNSUCCESSFUL. File ID: " + id);
            throw new FileNotFoundException("File not found");
        }
        uploadedFileRepo.deleteById(id);
        logger.info("File DELETED. File ID: " + id);
    }

    @Override
    public Optional<UploadedFile> getUploadedFileById(long id) {
        logger.info("File SEARCH. File ID: " + id);
        return uploadedFileRepo.findById(id);
    }

    @Override
    public List<UploadedFile> getAllUploadedFiles() {
        logger.info("File GET ALL.");
        return uploadedFileRepo.findAll();
    }

    @Override
    public ExcelProcessResults processUploadedFile(long id) throws IOException {
        ExcelProcessResults excelProcessResults;
        UploadedFile uploadedFile = uploadedFileRepo.findById(id).orElseThrow(FileNotFoundException::new);
        excelProcessResults = UploadedFileProcessService.processUploadedFile(uploadedFile);
        logger.info("File PROCESSED. File ID: " + id);
        return excelProcessResults;
    }

    @Override
    public long getUploadedFileCount() {
        logger.info("File COUNT performed.");
        return uploadedFileRepo.count();
    }


}
