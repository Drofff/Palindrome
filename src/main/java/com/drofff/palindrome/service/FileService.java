package com.drofff.palindrome.service;

public interface FileService {

	void saveFile(String filename, byte[] data);

	byte[] getFileByName(String filename);

}