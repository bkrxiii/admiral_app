package uk.ac.cardiff.phoenix.ilm.developmentConfig;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import uk.ac.cardiff.phoenix.ilm.model.UploadedFile;
import uk.ac.cardiff.phoenix.ilm.repository.UploadedFileRepo;
import uk.ac.cardiff.phoenix.ilm.service.UploadedFileService;

import java.io.ByteArrayInputStream;

@Configuration
@Profile("dev")
class UploadedFilesDatabaseInit {

        private final UploadedFileService uploadedFileService;
        private final UploadedFileRepo uploadedFileRepo;


    UploadedFilesDatabaseInit(UploadedFileService uploadedFileService, UploadedFileRepo uploadedFileRepo) {
        this.uploadedFileService = uploadedFileService;
        this.uploadedFileRepo = uploadedFileRepo;
    }
    @Bean
    public CommandLineRunner loadDevDataUpload() {
        return args -> {
            UploadedFile uploadedFile1 = new UploadedFile("testFile.txt");
            uploadedFile1.streamContentToByte(streamContentToByte("This is a test file"));
            uploadedFileRepo.save(uploadedFile1);

            UploadedFile uploadedFile2 = new UploadedFile("testFile2.txt");
            uploadedFile2.streamContentToByte(streamContentToByte("This is a test file 2"));
            uploadedFileRepo.save(uploadedFile2);

            UploadedFile uploadedFile3 = new UploadedFile("testFile3.txt");
            uploadedFile3.streamContentToByte(streamContentToByte("This is a test file 3"));
            uploadedFileRepo.save(uploadedFile3);

            UploadedFile uploadedFile4 = new UploadedFile("testFile4.txt");
            uploadedFile4.streamContentToByte(streamContentToByte("This is a test file 4"));
            uploadedFileRepo.save(uploadedFile4);



        };
    }


    private ByteArrayInputStream streamContentToByte(String inputString) {
        byte[] stringBytes = inputString.getBytes();
        return new ByteArrayInputStream(stringBytes);
            }
}
