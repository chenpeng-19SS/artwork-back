package com.fsy2001.artwork.repository;

import com.fsy2001.artwork.exception.WebRequestException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;

@Repository
public class FileSystemRepository {
    String RESOURCE_DIR =
            Objects.requireNonNull(
                            FileSystemRepository.class.getResource("/"))
                    .getPath() + "public/";

    public String save(byte[] content, String imageName) throws IOException {
        Path newFile = Paths.get(RESOURCE_DIR + "image/" + new Date().getTime() + "-" + imageName);
        Files.createDirectories(newFile.getParent());

        Files.write(newFile, content);
        return "image/" + newFile.getFileName().toString();
    }

    public void delete(String imageName) throws IOException {
        Path fileToDelete = Paths.get(RESOURCE_DIR + imageName);
        Files.delete(fileToDelete);
    }

    public FileSystemResource findInFileSystem(String path) {
        try {
            return new FileSystemResource(Paths.get(path));
        } catch (Exception e) {
            throw new WebRequestException("file-not-exist", HttpStatus.NOT_FOUND);
        }
    }
}