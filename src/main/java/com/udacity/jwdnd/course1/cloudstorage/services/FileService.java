package com.udacity.jwdnd.course1.cloudstorage.services;

import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.udacity.jwdnd.course1.cloudstorage.exception.EntityNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.exception.ErrorException;
import com.udacity.jwdnd.course1.cloudstorage.exception.FileNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.repository.FileMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FileService {
    private FileMapper fileMapper;

    public File getUserFile(String fileName, Integer userId) {
        Optional<File> file = fileMapper.getUserFileByName(fileName, userId);
        return unwrapFile(file);
    }

    public List<File> getUserFiles(Integer userId) {
        return fileMapper.getUserFiles(userId);
    }

    public void uploadFile(File file) {
        Optional<File> existingFile = fileMapper.getUserFileByName(file.getFileName(), file.getUserId());
        if (existingFile.isPresent()) {
            throw new ErrorException("File already exists");
        }
        int result = fileMapper.insertFile(file);
        if (result != 1)
            throw new ErrorException("An error occurred while trying to upload this file");

    }

    public void deleteFile(String fileName, Integer userId) {
        int result = fileMapper.deleteFile(fileName, userId);
        if (result != 1)
            throw new ErrorException("An error occurred while trying to delete this file");
    }

    static File unwrapFile(Optional<File> file) {
        if (file.isPresent())
            return file.get();
        else
            throw new FileNotFoundException("Sorry, can't find File");
    }

}
