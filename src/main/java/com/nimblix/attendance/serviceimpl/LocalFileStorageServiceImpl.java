//package com.nimblix.attendance.serviceimpl;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.UUID;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.nimblix.attendance.exception.BadRequestException;
//import com.nimblix.attendance.service.FileStorageService;
//
//@Service
//public class LocalFileStorageServiceImpl implements FileStorageService {
//
//    @Value("${file.upload-dir:uploads}")
//    private String uploadDir;
//
//    @Override
//    public String save(MultipartFile file) throws IOException {
//
//        if (file == null || file.isEmpty()) {
//            throw new BadRequestException("File is empty");
//        }
//
//        // Create upload directory if it doesn't exist
//        Path dirPath = Paths.get(uploadDir);
//        if (!Files.exists(dirPath)) {
//            Files.createDirectories(dirPath);
//        }
//
//        // Generate unique filename to avoid collisions
//        String extension = getFileExtension(file.getOriginalFilename());
//        String filename = UUID.randomUUID() + (extension.isEmpty() ? "" : "." + extension);
//
//        Path filePath = dirPath.resolve(filename);
//        file.transferTo(filePath.toFile());
//        
//
//        // Return path relative to server
//        return "/" + uploadDir + "/" + filename;
//    }
//
//    @Override
//    public boolean delete(String path) {
//        if (path == null || path.isBlank()) return false;
//
//        File file = new File(path.replaceFirst("/", "")); // remove leading slash
//        if (file.exists()) {
//            return file.delete();
//        }
//        return false;
//    }
//
//    private String getFileExtension(String filename) {
//        if (filename == null || !filename.contains(".")) return "";
//        return filename.substring(filename.lastIndexOf('.') + 1);
//    }
//
//    @Override
//    public String getFileUrl(String storedPath) {
//        if (storedPath == null) return null;
//        // Assuming server serves static resources from /uploads/**
//        //production return s3 URL
//        return "http://localhost:8080" + storedPath;
//    }
//
//}

package com.nimblix.attendance.serviceimpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.nimblix.attendance.service.FileStorageService;

@Service
public class LocalFileStorageServiceImpl implements FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Override
    public String save(MultipartFile file) throws IOException {
        return save(file, "attendance", file.getOriginalFilename());
    }

    @Override
    public String save(MultipartFile file, String folder, String filename) throws IOException {

        String cleanFilename = StringUtils.cleanPath(filename);
        Path folderPath = Paths.get(uploadDir, folder);
        Files.createDirectories(folderPath);

        Path filePath = folderPath.resolve(cleanFilename);
        Files.copy(
        	    file.getInputStream(),
        	    filePath,
        	    StandardCopyOption.REPLACE_EXISTING
        	);


        return folder + "/" + cleanFilename; // relative path
    }

    @Override
    public File getFileAsFile(String relativePath) {
        return Paths.get(uploadDir).resolve(relativePath).toFile();
    }

    @Override
    public String getFileUrl(String relativePath) {
        if (relativePath == null) return null;
        return "/files/" + relativePath;
    }
}

