package com.hobbyFinder.hubby.repositories;

import java.util.UUID;

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


    @Query("SELECT AVG(a.stars) FROM Participation p JOIN p.avaliation a where p.idEvent = :eventId")
    double avgStarsByEvent(UUID eventId);
}
