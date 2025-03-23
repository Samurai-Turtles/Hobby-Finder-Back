package com.hobbyFinder.hubby.controller;

import com.hobbyFinder.hubby.controller.routes.PhotoRoutes;
import com.hobbyFinder.hubby.services.ServicesImpl.PhotoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping(PhotoRoutes.PHOTO_BY_USER_LOGGED)
    public void postByUser(MultipartFile file) {
        this.photoService.uploadUserPhoto(file);
    }

    @PostMapping(PhotoRoutes.PHOTO_BY_EVENT)
    public void postByEvent(UUID id, MultipartFile file) {
        this.photoService.uploadEventPhoto(id, file);
    }

    @PostMapping(PhotoRoutes.PHOTO_BY_ID)
    public byte[] getPhoto(UUID id) {
        return this.photoService.GetPhotoById(id);
    }


}
