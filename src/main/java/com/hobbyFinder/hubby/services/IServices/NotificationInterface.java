package com.hobbyFinder.hubby.services.IServices;

import com.hobbyFinder.hubby.models.entities.Event;
import com.hobbyFinder.hubby.models.entities.User;

import java.util.UUID;

public interface NotificationInterface {

    void notifyChangeEvent(Event event);

    void notifySolicitation(User user, Event event);

    void notifyAproveSolicitation(User user, Event event);

    void notifyConfirmParticipation(User user, Event event);

}
