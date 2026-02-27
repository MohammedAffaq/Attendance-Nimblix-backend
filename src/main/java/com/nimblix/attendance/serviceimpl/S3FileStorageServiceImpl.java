//package com.nimblix.attendance.serviceimpl;
//
//import java.io.IOException;
//import java.util.UUID;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Profile;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.nimblix.attendance.exception.BadRequestException;
//import com.nimblix.attendance.service.FileStorageService;
//
//import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
//import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//import software.amazon.awssdk.services.s3.model.S3Exception;
//import software.amazon.awssdk.core.sync.RequestBody;
//
//@Service
//@Profile("prod") // Only active in production profile
//public class S3FileStorageServiceImpl implements FileStorageService {
//
//	private final S3Client s3;
//	private final String bucketName;
//
//	public S3FileStorageServiceImpl(@Value("${aws.access-key}") String accessKey,
//			@Value("${aws.secret-key}") String secretKey, @Value("${aws.s3.region}") String region,
//			@Value("${aws.s3.bucket}") String bucketName) {
//		this.bucketName = bucketName;
//
//		AwsBasicCredentials creds = AwsBasicCredentials.create(accessKey, secretKey);
//		this.s3 = S3Client.builder().region(Region.of(region))
//				.credentialsProvider(StaticCredentialsProvider.create(creds)).build();
//	}
//
//	@Override
//	public String save(MultipartFile file) throws IOException {
//
//		if (file == null || file.isEmpty()) {
//			throw new BadRequestException("File is empty");
//		}
//
//		String extension = getFileExtension(file.getOriginalFilename());
//		String filename = UUID.randomUUID() + (extension.isEmpty() ? "" : "." + extension);
//
//		try {
//			PutObjectRequest putRequest = PutObjectRequest.builder().bucket(bucketName).key(filename)
//					.contentType(file.getContentType()).build();
//
//			s3.putObject(putRequest, RequestBody.fromBytes(file.getBytes()));
//		} catch (S3Exception e) {
//			throw new RuntimeException("Failed to upload file to S3", e);
//		}
//
//		// Return public URL (adjust if using CloudFront or private bucket)
//		return "https://" + bucketName + ".s3.amazonaws.com/" + filename;
//	}
//
//	@Override
//	public boolean delete(String path) {
//		// Implement S3 delete if needed
//		return false;
//	}
//
//	private String getFileExtension(String filename) {
//		if (filename == null || !filename.contains("."))
//			return "";
//		return filename.substring(filename.lastIndexOf('.') + 1);
//	}
//
//	@Override
//	public String getFileUrl(String storedPath) {
//		if (storedPath == null)
//			return null;
//		// storedPath is already the full S3 URL
//		return storedPath;
//	}
//
//}
