package com.hobbyFinder.hubby.services.ServicesImpl;

import com.hobbyFinder.hubby.exception.NotFound.EventNotFoundException;
import com.hobbyFinder.hubby.infra.s3.PhotoRepository;
import com.hobbyFinder.hubby.models.entities.Event;
import com.hobbyFinder.hubby.models.entities.Photo;
import com.hobbyFinder.hubby.models.entities.User;
import com.hobbyFinder.hubby.repositories.EventRepository;
import com.hobbyFinder.hubby.repositories.UserRepository;
import com.hobbyFinder.hubby.services.IServices.PhotoInterface;
import com.hobbyFinder.hubby.util.GetUserLogged;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@AllArgsConstructor
public class PhotoService implements PhotoInterface {

    private final PhotoRepository photoRepository;

    private final GetUserLogged getUserLogged;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    @Override
    public void uploadUserPhoto(MultipartFile file) {
        User user = this.getUserLogged.getUserLogged();

        Photo photo = user.getPhoto();

        this.photoRepository.upload(photo.getId(), file);

        photo.setSaved(true);

        photo.setExtension(file.getContentType());

        this.userRepository.save(user);
    }

    @Override
    public void uploadEventPhoto(UUID id, MultipartFile file) {
        Event event = this.eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(""));

        Photo photo = event.getPhoto();

        this.photoRepository.upload(photo.getId(), file);

        photo.setSaved(true);

        photo.setExtension(file.getContentType());

        this.eventRepository.save(event);
    }

    @Override
    public byte[] GetPhotoById(UUID id) {
        return this.photoRepository.getFile(id);
    }
}
