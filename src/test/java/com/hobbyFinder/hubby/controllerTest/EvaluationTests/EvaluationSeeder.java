package com.hobbyFinder.hubby.controllerTest.EvaluationTests;

import com.hobbyFinder.hubby.controllerTest.EventTests.EventSeeder;
import com.hobbyFinder.hubby.controllerTest.UserTests.UserSeeder;
import com.hobbyFinder.hubby.models.entities.Evaluation;
import com.hobbyFinder.hubby.models.entities.Participation;
import com.hobbyFinder.hubby.models.entities.User;
import com.hobbyFinder.hubby.models.enums.ParticipationPosition;
import com.hobbyFinder.hubby.models.enums.UserParticipation;
import com.hobbyFinder.hubby.repositories.EventRepository;
import com.hobbyFinder.hubby.repositories.ParticipationRepository;
import com.hobbyFinder.hubby.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component
public class EvaluationSeeder {


    @Autowired
    UserSeeder userSeeder;
    @Autowired
    EventSeeder eventSeeder;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    ParticipationRepository participationRepository;
    @Autowired
    UserRepository userRepository;

    private UUID idFirstEvent;
    private UUID idSecondEvent;

    void seedEvaluations() throws Exception {
        userSeeder.seedUsers();
        eventSeeder.seedEvents();
        this.idFirstEvent = getFirstEventId();
        this.idSecondEvent = getSecondEventId();
        seed();
    }

    protected void seed() throws Exception {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User user = userRepository.save(User.builder().participations(new ArrayList<>()).build());
            users.add(user);
        }

        for (int i = 0; i < 5; i++) {
            createParticipation(this.idFirstEvent, users.get(i).getId(), Evaluation
                    .builder()
                    .stars(i)
                    .comment(EvaluationConstants.COMMENT)
                    .user(users.get(i))
                    .build());
            createParticipation(this.idSecondEvent, users.get(i).getId(), Evaluation
                    .builder()
                    .stars(i + 1)
                    .comment(EvaluationConstants.COMMENT)
                    .user(users.get(i))
                    .build());
        }
    }

    public UUID getFirstEventId() {
        return eventRepository.findAll().get(0).getId();
    }
    public UUID getSecondEventId() {
        return eventRepository.findAll().get(1).getId();
    }

    private void createParticipation(UUID idEvent, UUID idUser, Evaluation evaluation) throws Exception {
        Participation newParticipation =
                Participation.builder()
                        .idEvent(idEvent)
                        .idUser(idUser)
                        .evaluation(evaluation)
                        .userParticipation(UserParticipation.UNCONFIRMED_PRESENCE).position(ParticipationPosition.PARTICIPANT)
                        .build();

        evaluation.setParticipation(newParticipation);

        participationRepository.save(newParticipation);
        eventRepository.findById(idEvent).get().getParticipations().add(newParticipation);
        eventRepository.findById(idEvent).get().setAvaliationStars(this.participationRepository.avgStarsByEvent(idEvent));
        eventRepository.flush();
        userRepository.findById(idUser).get().getParticipations().add(newParticipation);
        userRepository.flush();
        UUID idEventCreator = eventRepository.findById(idEvent).get().getCreator().getId();
        userRepository.findById(idEventCreator).get().setStars(this.participationRepository.findAverageStarsByUser(idEventCreator));
    }
}
