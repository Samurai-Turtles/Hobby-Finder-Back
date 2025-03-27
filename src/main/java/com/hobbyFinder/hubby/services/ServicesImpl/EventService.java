package com.hobbyFinder.hubby.services.ServicesImpl;

import com.hobbyFinder.hubby.exception.EventException.InvalidCapacityException;
import com.hobbyFinder.hubby.exception.EventException.InvalidDateException;
import com.hobbyFinder.hubby.exception.EventException.UserNotEventPermissionException;
import com.hobbyFinder.hubby.exception.NotFound.EventNotFoundException;
import com.hobbyFinder.hubby.models.dto.events.EventDto;
import com.hobbyFinder.hubby.models.dto.events.EventPutDto;
import com.hobbyFinder.hubby.models.dto.events.LocalDto;
import com.hobbyFinder.hubby.models.dto.participations.ParticipationCreateDto;
import com.hobbyFinder.hubby.models.dto.photos.PhotoDto;
import com.hobbyFinder.hubby.models.entities.*;
import com.hobbyFinder.hubby.models.enums.ParticipationPosition;
import com.hobbyFinder.hubby.models.enums.PrivacyEnum;
import com.hobbyFinder.hubby.models.enums.UserParticipation;
import com.hobbyFinder.hubby.repositories.ParticipationRepository;
import com.hobbyFinder.hubby.repositories.UserRepository;
import com.hobbyFinder.hubby.controller.Constants.PageConstants;
import com.hobbyFinder.hubby.util.GetUserLogged;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.hobbyFinder.hubby.models.dto.events.EventCreateDto;
import com.hobbyFinder.hubby.repositories.EventRepository;
import com.hobbyFinder.hubby.services.IServices.EventInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EventService implements EventInterface {

    private EventRepository eventRepository;

    private ParticipationRepository participationRepository;

    private UserRepository userRepository;

    @Lazy
    private GetUserLogged getUserLogged;

    private NotificationService notificationService;

    @Override
    public EventDto registerEvent(EventCreateDto eventCreateDto) {
        User userCreated = getUserLogged.getUserLogged();

        checkValidData(eventCreateDto.begin(), eventCreateDto.end());

        Local local = Local.builder().street(eventCreateDto.local().street())
                .district(eventCreateDto.local().district())
                .number(eventCreateDto.local().number())
                .city(eventCreateDto.local().city())
                .state(eventCreateDto.local().state()).build();

        Event event = Event.builder().name(eventCreateDto.Name())
                .EventBegin(eventCreateDto.begin())
                .EventEnd(eventCreateDto.end())
                .local(local)
                .privacy(eventCreateDto.privacy())
                .description(eventCreateDto.description())
                .maxUserAmount(eventCreateDto.maxUserAmount())
                .participations(new ArrayList<Participation>())
                .photo(new Photo())
                .creator(userCreated)
                .build();

        this.eventRepository.save(event);

        Participation participation = newParticipation(
                new ParticipationCreateDto(event.getId(), getUserLogged.getUserLogged().getId(),
                        UserParticipation.CONFIRMED_PRESENCE, ParticipationPosition.CREATOR));
        event.getParticipations().add(participation);

        User user = getUserLogged.getUserLogged();
        user.getParticipations().add(participation);
        this.userRepository.save(user);
        this.eventRepository.save(event);
        return postEventDto(event, local);
    }

    private EventDto postEventDto(Event event, Local local) {
        LocalDto localDto = new LocalDto(local.getStreet(), local.getDistrict(), local.getNumber(), local.getCity(), local.getState(), local.getLongitude(), local.getLatitude());
        Photo photo = event.getPhoto();
        PhotoDto photoDto = new PhotoDto(photo.getId(), photo.getExtension(), photo.isSaved());
        return new EventDto(event.getId(), event.getName(), event.getEventBegin(), event.getEventEnd(), localDto,
                event.getPrivacy(), event.getDescription(), event.getMaxUserAmount(), event.getParticipations().size(),
                photoDto);
    }

    private void checkValidData(LocalDateTime begin, LocalDateTime end) {
        if (begin.isAfter(end)) {
            throw new InvalidDateException();
        }
    }

    private Participation newParticipation(ParticipationCreateDto participationDTO) {
        Participation participation = Participation.builder().idEvent(participationDTO.idEvent())
                .idUser(participationDTO.idUser())
                .userParticipation(participationDTO.userParticipation())
                .position(participationDTO.participationPosition())
                .evaluation(null)
                .build();
        participationRepository.save(participation);
        return participation;
    }

    @Override
    public Event findEvent(UUID idEvent) {
        return eventRepository.findById(idEvent)
                .orElseThrow(() -> new EventNotFoundException("Evento não encontrado."));
    }

    @Override
    public void checkPermission(UUID idEvent) {
        Event event = findEvent(idEvent);
        User user = getUserLogged.getUserLogged();
        boolean isCreator = event.getCreator().isSameUser(user);
        if (!isCreator) {
            throw new UserNotEventPermissionException();
        }
    }
    public void deleteEvent(UUID uuid) {
        checkPermission(uuid);
        eventRepository.delete(findEvent(uuid));
        eventRepository.flush();
    }

    @Override
    public void updateEventAvaliation(UUID idEvent, double stars) {
        Event event = findEvent(idEvent);
        event.setAvaliationStars(stars);
        this.eventRepository.save(event);
    }

    @Override
    public boolean checkUserParticipating(Event event) {
        return  event.getParticipations()
                .stream()
                .anyMatch(p -> p.getIdUser().equals(getUserLogged.getUserLogged().getId()));
    }

    @Override
    public EventDto updateEvent(UUID id, EventPutDto eventPutDto) {
        checkPermission(id);
        Event event = findEvent(id);
        if (eventPutDto.Name() != null) {
            event.setName(eventPutDto.Name());
        }
        if (eventPutDto.begin() != null && eventPutDto.end() != null) {
            checkValidData(eventPutDto.begin(), eventPutDto.end());
            event.setEventBegin(eventPutDto.begin());
            event.setEventEnd(eventPutDto.end());
        } else if (eventPutDto.begin() != null) {
            checkValidData(eventPutDto.begin(), event.getEventEnd());
            event.setEventBegin(eventPutDto.begin());
        } else if (eventPutDto.end() != null) {
            checkValidData(event.getEventBegin(), eventPutDto.end());
            event.setEventEnd(eventPutDto.end());
        }
        if (eventPutDto.local() != null) {
            Local local = Local.builder().street(eventPutDto.local().street())
                    .district(eventPutDto.local().district())
                    .number(eventPutDto.local().number())
                    .city(eventPutDto.local().city())
                    .state(eventPutDto.local().state()).build();
            event.setLocal(local);
        }
        if (eventPutDto.privacy() != null) {
            event.setPrivacy(eventPutDto.privacy());
        }
        if (eventPutDto.description() != null) {
            event.setDescription(eventPutDto.description());
        }
        if (eventPutDto.maxUserAmount() != null ) {
            if(eventPutDto.maxUserAmount() < event.getParticipations().size()) {
                throw new InvalidCapacityException();
            }
            event.setMaxUserAmount(eventPutDto.maxUserAmount());
        }
        eventRepository.save(event);

        notificationService.notifyChangeEvent(event);

        Local local = event.getLocal();
        LocalDto localDto = new LocalDto(local.getStreet(), local.getDistrict(), local.getNumber(), local.getCity(), local.getState(), local.getLongitude(), local.getLatitude());

        Photo photo = event.getPhoto();

        PhotoDto photoDto = new PhotoDto(photo.getId(), photo.getExtension(), photo.isSaved());

        return new EventDto(event.getId(), event.getName(), event.getEventBegin(), event.getEventEnd(), localDto,
                event.getPrivacy(), event.getDescription(), event.getMaxUserAmount(), event.getParticipations().size(),
                photoDto);
    }


    @Override
    public EventDto getEvent(UUID id) {
        Event event = findEvent(id);

        boolean isParticipating = checkUserParticipating(event);
        boolean isPrivateEvent = event.getPrivacy().equals(PrivacyEnum.PRIVATE);

        Photo photo = event.getPhoto();
        PhotoDto photoDto = new PhotoDto(photo.getId(), photo.getExtension(), photo.isSaved());

        if (!isParticipating && isPrivateEvent)
        {
            return new EventDto(event.getId(), event.getName(), null, null, null,
                    event.getPrivacy(), null, 0, 0,
                    photoDto);
        }

        Local local = event.getLocal();

        LocalDto localDto = new LocalDto(local.getStreet(), local.getDistrict(), local.getNumber(), local.getCity(), local.getState(), local.getLongitude(), local.getLatitude());

        return new EventDto(event.getId(), event.getName(), event.getEventBegin(), event.getEventEnd(), localDto,
                event.getPrivacy(), event.getDescription(), event.getMaxUserAmount(), event.getParticipations().size(),
                photoDto);
    }

    @Override
    public Page<EventDto> getEventByAuthUser(Optional<Double> latitude, Optional<Double> longitude, Optional<String> name, Pageable pageable) {
        boolean isLatitudePresent = latitude.isPresent();
        boolean isLongitudePresent = longitude.isPresent();
        String prefix = name.orElse(PageConstants.Prefix) + "%";

        Page<Event> retorno;
        if (isLongitudePresent && isLatitudePresent) {
            double realLatitude  = latitude.get();
            double realLongitude = longitude.get();
            retorno = eventRepository.findEventsByLatitudeLongitude(realLatitude, realLongitude, prefix, pageable);
        }
        else {
            retorno = eventRepository.findEventsByName(prefix, pageable);
        }

        return retorno.map(event -> postEventDto(event, event.getLocal()));

    }

    @Override
    public Page<EventDto> getByUserId(UUID userId, Optional<String> name, Pageable pageable) {
        String prefix = name.orElse(PageConstants.Prefix) + "%";

        Page<Event> retorno = eventRepository.findByUserId(userId, prefix, pageable);

        return retorno.map(event -> postEventDto(event, event.getLocal()));
    }
}
