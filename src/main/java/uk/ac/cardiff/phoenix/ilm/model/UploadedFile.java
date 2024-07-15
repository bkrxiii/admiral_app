package uk.ac.cardiff.phoenix.ilm.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;


@Data
@Entity
@NoArgsConstructor
@Audited
public class UploadedFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String fileName;
    private String originalFilename;
    private LocalDateTime uploadDateTime;
    @Lob @Basic(fetch = FetchType.LAZY)
    @Column(name = "content", columnDefinition = "BLOB", length = 16777215  )
    private byte[] content;

    public UploadedFile(String fileName) {
        this.fileName = fileName;
        this.originalFilename = fileName;
        this.uploadDateTime = LocalDateTime.now();
    }

    public UploadedFile updateFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }


    public void streamContentToByte(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024]; // or any other buffer size

        int nRead;
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        this.content = buffer.toByteArray();
    }

    public void streamFileToByte(File file) throws IOException {
        this.content = Files.readAllBytes(file.toPath());
    }






}
