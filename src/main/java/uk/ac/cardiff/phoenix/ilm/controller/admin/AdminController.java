package uk.ac.cardiff.phoenix.ilm.controller.admin;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.cardiff.phoenix.ilm.excelProcessor.ExcelProcessResults;
import uk.ac.cardiff.phoenix.ilm.model.UploadedFile;
import uk.ac.cardiff.phoenix.ilm.service.UploadedFileService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RequestMapping("/admin")
@Controller
@RequiredArgsConstructor
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final UploadedFileService uploadedFileService;

    @GetMapping("/")
    public ModelAndView admin() {
        ModelAndView modelAndView = new ModelAndView("admin/home");
        modelAndView.addObject("title_insert", "Admin");
        return modelAndView;
    }

    @GetMapping("/upload")
    public ModelAndView upload() {
        ModelAndView modelAndView = new ModelAndView("admin/upload");
        modelAndView.addObject("title_insert", "Upload Data");
        List<UploadedFile> uploadedFiles = uploadedFileService.getAllUploadedFiles();
        modelAndView.addObject("uploadedFiles", uploadedFiles);
        return modelAndView;
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/admin/upload";
        }

        try {
            System.out.println("Received file: " + file.getOriginalFilename());
            uploadedFileService.saveUploadedFile(file);
            // Add a success message
            redirectAttributes.addFlashAttribute("message", "File uploaded successfully!");

        } catch (Exception e) {
            logger.error("Error uploading file", e);

            // Handle exceptions if needed
            redirectAttributes.addFlashAttribute("message", "File upload failed.");
        }

        return "redirect:/admin/upload";
    }

    @GetMapping("/upload/delete/{id}")
    public String deleteFile(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        try {
            uploadedFileService.deleteUploadedFile(id);
        } catch (FileNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", "No file found with id: " + id);
            return "redirect:/admin/upload";
        }
        redirectAttributes.addFlashAttribute("message", "File deleted successfully!");
        return "redirect:/admin/upload";
    }

    @GetMapping("/upload/process/{id}")
    public String processFile(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        ExcelProcessResults excelProcessResults;
        try {
            excelProcessResults = uploadedFileService.processUploadedFile(id);
        } catch (FileNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", "No file found with id: " + id);
            return "redirect:/admin/upload";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "Error processing file: " + uploadedFileService.getUploadedFileById(id).get().getOriginalFilename());
            return "redirect:/admin/upload";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("message", "Excel document doesn't match any known structure");
            return "redirect:/admin/upload";
        }
        redirectAttributes.addFlashAttribute("message", excelProcessResults.getDataTypeProcessed() + " File processed successfully! "+excelProcessResults.getSuccessRows()+" records processed. " + excelProcessResults.getFailRows() + " records failed.");
        return "redirect:/admin/upload";
    }


    @GetMapping("/upload/download/{id}")
    public ResponseEntity<StreamingResponseBody> downloadFile(@PathVariable("id") long id, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        Optional<UploadedFile> optionalUploadedFile = uploadedFileService.getUploadedFileById(id);

        if (optionalUploadedFile.isPresent()) {
            UploadedFile uploadedFile = optionalUploadedFile.get();
            byte[] fileContent = uploadedFile.getContent();

            String filename = uploadedFile.getOriginalFilename();

            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

            StreamingResponseBody responseBody = outputStream -> {
                try {
                    outputStream.write(fileContent);
                } catch (IOException e) {
                    // Handle the exception if needed
                }
            };

            return ResponseEntity.ok().body(responseBody);
        } else {
            redirectAttributes.addFlashAttribute("message", "No such file found");
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("/admin/upload"))
                    .build();
        }
    }
}


