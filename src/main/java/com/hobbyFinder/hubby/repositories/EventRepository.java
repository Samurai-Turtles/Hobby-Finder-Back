package com.hobbyFinder.hubby.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hobbyFinder.hubby.models.entities.Event;
import java.util.List;
import com.hobbyFinder.hubby.models.entities.Local;
import com.hobbyFinder.hubby.models.enums.PrivacyEnum;



@Repository
public interface EventRepository extends JpaRepository<Event, UUID>{
    
    Event findByName(String name);
    List<Event> findByLocal(Local local);
    List<Event> findByPrivacy(PrivacyEnum privacy);
    List<Event> findByMaxUserAmount(int maxUserAmout);
    List<Event> findByDescription(String description);


    @Query("SELECT AVG(a.stars) FROM Participation p JOIN p.evaluation a where p.idEvent = :eventId")
    double avgStarsByEvent(UUID eventId);

    @Query("SELECT event " +
            "FROM Event event " +
            "WHERE event.name LIKE :Prefix " +
            "ORDER BY (abs(event.local.latitude - :latitude) + abs(event.local.longitude - :longitude)) ASC ")
    Page<Event> findEventsByLatitudeLongitude(double latitude, double longitude, String prefix, Pageable page);

    @Query("SELECT event FROM Event event WHERE event.name LIKE :Prefix")
    Page<Event> findEventsByName(String prefix, Pageable page);

    @Query("SELECT event " +
            "FROM Event event " +
            "WHERE event.id in (" +
            "   SELECT participation.idEvent " +
            "   FROM Participation participation " +
            "   WHERE participation.idUser = :userId" +
            ") AND event.name LIKE :prefix")
    Page<Event> findByUserId(UUID userId, String prefix, Pageable page);
}
