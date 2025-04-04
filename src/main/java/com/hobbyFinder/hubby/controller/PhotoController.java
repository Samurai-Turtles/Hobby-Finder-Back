package com.hobbyFinder.hubby.controller;

import com.hobbyFinder.hubby.controller.routes.PhotoRoutes;
import com.hobbyFinder.hubby.services.ServicesImpl.PhotoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping(value = PhotoRoutes.PHOTO_BY_USER_LOGGED, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void postByUser(@RequestParam("file") MultipartFile file) {
        this.photoService.uploadUserPhoto(file);
    }

    @PostMapping(value = PhotoRoutes.PHOTO_BY_EVENT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void postByEvent(
            UUID id,
            @RequestParam("file")MultipartFile file) {
        this.photoService.uploadEventPhoto(id, file);
    }

    @GetMapping(PhotoRoutes.PHOTO_BY_ID)
    public ResponseEntity<byte[]> getPhoto(@PathVariable UUID id) {
        byte[] photo = this.photoService.GetPhotoById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return ResponseEntity.ok()
                .headers(headers)
                .body(photo);
    }


}
