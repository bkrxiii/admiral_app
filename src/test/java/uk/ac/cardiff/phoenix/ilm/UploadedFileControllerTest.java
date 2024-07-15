package uk.ac.cardiff.phoenix.ilm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import uk.ac.cardiff.phoenix.ilm.controller.admin.AdminController;
import uk.ac.cardiff.phoenix.ilm.programs.service.LevelsService;
import uk.ac.cardiff.phoenix.ilm.programs.service.WorkshopsService;
import uk.ac.cardiff.phoenix.ilm.service.UploadedFileService;

import java.io.FileNotFoundException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(controllers = AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UploadedFileControllerTest {

    @MockBean
    private UploadedFileService uploadedFileService;

    //needed because of the @Controller Advice requiring these
    @MockBean
    private LevelsService levelsService;
    @MockBean
    private WorkshopsService workSearchService;


    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test

    public void correctlyHandleFileNotFound() throws Exception {
        long id = 1L;
        when(uploadedFileService.processUploadedFile(id)).thenThrow(new FileNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/upload/process/{id}", id))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "No file found with id: " + id))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/upload"));
    }

    @Test

    public void handleSuccessfulupload() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test data".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/admin/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "File uploaded successfully!"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/upload"));
    }
}