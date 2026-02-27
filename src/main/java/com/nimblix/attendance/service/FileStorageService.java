package com.nimblix.attendance.service;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String save(MultipartFile file) throws IOException;

    String save(MultipartFile file, String folder, String filename) throws IOException;

    File getFileAsFile(String relativePath);

    String getFileUrl(String relativePath);
}
