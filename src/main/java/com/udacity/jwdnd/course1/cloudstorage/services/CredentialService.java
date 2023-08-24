package com.udacity.jwdnd.course1.cloudstorage.services;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.udacity.jwdnd.course1.cloudstorage.exception.EntityNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.exception.ErrorException;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.repository.CredentialMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CredentialService {
    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public Credential getUserCredential(Integer credentialId, Integer userId) {
        Optional<Credential> credential = credentialMapper.getUserCredentialById(credentialId, userId);
        Credential currentCredential = unwrapCredential(credential, credentialId);
        currentCredential.setPassword(
                encryptionService.decryptValue(currentCredential.getPassword(), currentCredential.getKey()));
        return currentCredential;
    }

    public List<Credential> getUserCredentials(Integer userId) {
        return credentialMapper.getUserCredentials(userId);

    }

    public void save(Credential credential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encryptionKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encryptionKey);
        credential.setKey(encryptionKey);
        credential.setPassword(encryptedPassword);
        int result = credentialMapper.insert(new Credential(null,
                credential.getUrl(), credential.getUsername(),
                credential.getKey(), credential.getPassword(), credential.getUserId()));

        if (result != 1)
            throw new ErrorException("An error occurred while trying to save a credential");
    }

    public void delete(Integer credentialId, Integer userId) {
        int result = credentialMapper.deleteCredential(credentialId, userId);
        if (result != 1)
            throw new ErrorException("An error occurred while trying to delete this credential");

    }

    public void update(Credential credential) {
        int result = credentialMapper.updateCredential(credential);

        if (result != 1)
            throw new ErrorException("Unable to Update Credential: " + credential.getCredentialId());

    }

    static Credential unwrapCredential(Optional<Credential> entity, Integer id) {
        if (entity.isPresent())
            return entity.get();
        else
            throw new EntityNotFoundException(id, Credential.class);
    }
}
