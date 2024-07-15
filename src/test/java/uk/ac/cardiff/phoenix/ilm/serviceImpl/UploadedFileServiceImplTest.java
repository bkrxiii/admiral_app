package uk.ac.cardiff.phoenix.ilm.serviceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.cardiff.phoenix.ilm.excelProcessor.ExcelProcessResults;
import uk.ac.cardiff.phoenix.ilm.excelProcessor.ExcelTypeMapping;
import uk.ac.cardiff.phoenix.ilm.model.UploadedFile;
import uk.ac.cardiff.phoenix.ilm.repository.UploadedFileRepo;
import uk.ac.cardiff.phoenix.ilm.service.UploadedFileProcessService;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@AutoConfigureTestDatabase
public class UploadedFileServiceImplTest {

    @Mock
    private UploadedFileRepo uploadedFileRepo;
    @Mock
    private UploadedFileProcessService uploadedFileProcessService;
    @Mock
    private MultipartFile file;

    private UploadedFileServiceImpl uploadedFileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        uploadedFileService = new UploadedFileServiceImpl(uploadedFileRepo, uploadedFileProcessService);
    }

    @Test
    void testDeleteExistingFile() throws FileNotFoundException {
        when(uploadedFileRepo.findById(1L)).thenReturn(Optional.of(new UploadedFile("testFile.xlsx")));

        uploadedFileService.deleteUploadedFile(1L);

        verify(uploadedFileRepo, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNonExistingFile() {
        when(uploadedFileRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FileNotFoundException.class, () -> uploadedFileService.deleteUploadedFile(1L));
    }


    @Test
    void testSaveUploadedFile() throws IOException {
        when(file.getOriginalFilename()).thenReturn("testFile.xlsx");
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("file content".getBytes()));
        uploadedFileService.saveUploadedFile(file);
        verify(uploadedFileRepo, times(1)).save(any(UploadedFile.class));
    }

    @Test
    void testGetAllUploadedFiles() {
        // Arrange
        List<UploadedFile> mockFiles = List.of(
                new UploadedFile("file1.xlsx"),
                new UploadedFile("file2.xlsx"),
                new UploadedFile("file3.xlsx")
        );
        when(uploadedFileRepo.findAll()).thenReturn(mockFiles);
        when(uploadedFileRepo.count()).thenReturn((long) mockFiles.size());

        // Act
        List<UploadedFile> resultFiles = uploadedFileService.getAllUploadedFiles();

        // Assert
        assertNotNull(resultFiles, "The returned file list should not be null.");
        assertEquals(mockFiles.size(), resultFiles.size(), "The size of returned file list should match.");
        assertIterableEquals(mockFiles, resultFiles, "The returned file list should match the mock file list.");

        assertEquals(uploadedFileService.getUploadedFileCount(), resultFiles.size(), "The size of the returned file list should match .");
    }



    @Test
    void shouldFindUploadedFileWhenIdExists() {
        // Setting up a test file
        UploadedFile testFile = new UploadedFile("testFile.xlsx");
        // Simulating a scenario where the file is found in the repository
        when(uploadedFileRepo.findById(1L)).thenReturn(Optional.of(testFile));

        // Attempting to retrieve the file by its ID
        Optional<UploadedFile> foundFile = uploadedFileService.getUploadedFileById(1L);

        // Checking that the file is found and matches our test file
        assertTrue(foundFile.isPresent());
        assertEquals(testFile, foundFile.get());
    }

    @Test
    void shouldNotFindUploadedFileWhenIdDoesNotExist() {
        // Simulating a scenario where the file does not exist in the repository
        when(uploadedFileRepo.findById(1L)).thenReturn(Optional.empty());

        // Attempting to retrieve a file by an ID that does not exist
        Optional<UploadedFile> foundFile = uploadedFileService.getUploadedFileById(1L);

        // Checking that no file is found
        assertFalse(foundFile.isPresent());
    }

    @Test
    void testProcessUploadedFileIntegration() throws IOException {
        // Given we have a file to be processed
        long fileId = 1L;
        UploadedFile testFile = new UploadedFile("testFile.xlsx");
        ExcelTypeMapping.ExcelDataType dataType = ExcelTypeMapping.ExcelDataType.CANDIDATE; // Replace SOME_DATA_TYPE with an actual data type
        ExcelProcessResults mockResults = new ExcelProcessResults(dataType);
        // When we attempt to process the file
        when(uploadedFileRepo.findById(fileId)).thenReturn(Optional.of(testFile));
        when(uploadedFileProcessService.processUploadedFile(testFile)).thenReturn(mockResults);

        //Then we get the expected results
        ExcelProcessResults results = uploadedFileService.processUploadedFile(fileId);


        verify(uploadedFileProcessService, times(1)).processUploadedFile(testFile);
        assertEquals(mockResults, results);
    }

    @Test
    void testProcessUploadedFileMocked() throws IOException {
        // Given we have a file to be processed
        long fileId = 1L;
        UploadedFile testFile = new UploadedFile("testFile.xlsx");
        when(uploadedFileRepo.findById(fileId)).thenReturn(Optional.of(testFile));

        // When we attempt to process the file
        uploadedFileService.processUploadedFile(fileId);

        // Then the correct method is called
        verify(uploadedFileProcessService, times(1)).processUploadedFile(testFile);
    }



}
