//package com.nimblix.attendance.serviceimpl;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.nimblix.attendance.service.FileStorageService;
//
//@Service
//public class FileStorageServiceImpl implements FileStorageService {
//
//	private final Path rootDir;
//
//	public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
//		this.rootDir = Paths.get(uploadDir).toAbsolutePath().normalize();
//		try {
//			Files.createDirectories(rootDir);
//		} catch (IOException e) {
//			throw new RuntimeException("Could not create upload directory", e);
//		}
//	}
//
//	public String save(MultipartFile file, String subDir, String filename) {
//		try {
//			Path targetDir = rootDir.resolve(subDir);
//			Files.createDirectories(targetDir);
//
//			Path targetPath = targetDir.resolve(filename);
//			file.transferTo(targetPath.toFile());
//
//			return "/" + subDir + "/" + filename;
//		} catch (IOException e) {
//			throw new RuntimeException("Failed to store file", e);
//		}
//	}
//}
