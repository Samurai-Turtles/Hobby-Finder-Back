package com.hobbyFinder.hubby.controllerTest.PartiticipationRequestTests;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.hobbyFinder.hubby.models.dto.events.EventCreateDto;
import com.hobbyFinder.hubby.models.dto.events.LocalDto;
import com.hobbyFinder.hubby.models.entities.Participation;
import com.hobbyFinder.hubby.models.entities.ParticipationRequest;
import com.hobbyFinder.hubby.models.enums.PrivacyEnum;

public class RequestConstants {

        public static final String STREET_EVENT1 = "Street 1";
        public static final String DISTRICT_EVENT1 = "District 1";
        public static final String NUMBER_EVENT1 = "Number 1";
        public static final String CITY_EVENT1 = "City 1";
        public static final String COUNTRY_EVENT1 = "Country 1";

        public static final String NAME_EVENT = "Futebol";
        public static final LocalDateTime DATE_TIME_EVENT_BEGIN = LocalDateTime.now().plusMonths(1);
        public static final LocalDateTime DATE_TIME_EVENT_END = LocalDateTime.now().plusMonths(2);
        public static final PrivacyEnum PRIVACY_ENUM1 = PrivacyEnum.PRIVATE;
        public static final String DESCRIPTION = "Description1";
        public static final List<ParticipationRequest> PARTICIPATION_REQUEST_LIST = new ArrayList<>();
        public static final int MAX_USER_AMOUNT = 5;
        public static final List<Participation> LOCAL_LIST = new ArrayList<>();

        public static final String NAME_EVENT2 = "Golfe";
        public static final PrivacyEnum PRIVACY_ENUM2 = PrivacyEnum.PRIVATE;
        public static final int MAX_USER_AMOUNT2 = 1;
        public static final List<ParticipationRequest> PARTICIPATION_REQUEST_LIST2 = new ArrayList<>();

        public static final LocalDto LOCAL_DTO = new LocalDto(STREET_EVENT1, DISTRICT_EVENT1, NUMBER_EVENT1,
                        CITY_EVENT1,
                        COUNTRY_EVENT1);

        public static final EventCreateDto EVENT_PRIVATE_MAX_5 = new EventCreateDto(NAME_EVENT, DATE_TIME_EVENT_BEGIN,
                        DATE_TIME_EVENT_END,
                        LOCAL_DTO, PRIVACY_ENUM1, DESCRIPTION, MAX_USER_AMOUNT);

        public static final EventCreateDto EVENT_PRIVATE_MAX_1 = new EventCreateDto(NAME_EVENT2, DATE_TIME_EVENT_BEGIN,
                        DATE_TIME_EVENT_END,
                        LOCAL_DTO, PRIVACY_ENUM2, DESCRIPTION, MAX_USER_AMOUNT2);

}
