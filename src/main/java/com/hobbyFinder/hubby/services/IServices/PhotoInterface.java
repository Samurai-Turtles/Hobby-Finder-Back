package com.hobbyFinder.hubby.services.IServices;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface PhotoInterface {

    void uploadUserPhoto(MultipartFile file);
    void uploadEventPhoto(UUID id, MultipartFile file);
    byte[] GetPhotoById(UUID id);

}
