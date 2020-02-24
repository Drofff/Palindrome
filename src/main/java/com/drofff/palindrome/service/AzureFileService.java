package com.drofff.palindrome.service;

import static com.drofff.palindrome.utils.ValidationUtils.validateNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.drofff.palindrome.configuration.properties.AzureProperties;
import com.drofff.palindrome.exception.PalindromeException;
import com.drofff.palindrome.exception.ValidationException;

@Service
public class AzureFileService implements FileService {

	private final AzureProperties azureProperties;

	private BlobContainerClient blobContainerClient;

	@Autowired
	public AzureFileService(AzureProperties azureProperties) {
		this.azureProperties = azureProperties;
	}

	@PostConstruct
	public void init() {
		BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
				.connectionString(azureProperties.getConnectionLink())
				.buildClient();
		blobContainerClient = blobServiceClient.getBlobContainerClient(azureProperties.getContainerName());
	}

	@Override
	public void saveFile(String filename, byte[] data) {
		validateFilename(filename);
		validateFileSize(data.length);
		BlobClient blobClient = blobContainerClient.getBlobClient(filename);
		ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		blobClient.upload(inputStream, data.length);
	}

	private void validateFileSize(int size) {
		if(isInvalidFileSize(size)) {
			throw new ValidationException("File is of size greater than maximum allowed size " + azureProperties.getMaxFileSize());
		}
	}

	private boolean isInvalidFileSize(int size) {
		return size > azureProperties.getMaxFileSizeBytes();
	}

	@Override
	public byte[] getFileByName(String filename) {
		validateFilename(filename);
		BlobClient blobClient = blobContainerClient.getBlobClient(filename);
		return downloadFileFromClient(blobClient);
	}

	private void validateFilename(String filename) {
		validateNotNull(filename, "Filename should be provided");
		validateFilenameNotEmpty(filename);
	}

	private void validateFilenameNotEmpty(String filename) {
		if(filename.isEmpty()) {
			throw new ValidationException("Filename should not be empty");
		}
	}

	private byte[] downloadFileFromClient(BlobClient blobClient) {
		try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			blobClient.download(outputStream);
			return outputStream.toByteArray();
		} catch(IOException e) {
			throw new PalindromeException("Error while fetching the file");
		}
	}

}
