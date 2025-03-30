package com.hobbyFinder.hubby.controllerTest.EventTests;

import com.hobbyFinder.hubby.controllerTest.UserTests.UserConstants;
import com.hobbyFinder.hubby.models.dto.events.EventCreateDto;
import com.hobbyFinder.hubby.models.dto.events.LocalDto;
import com.hobbyFinder.hubby.models.entities.Local;
import com.hobbyFinder.hubby.models.entities.Participation;
import com.hobbyFinder.hubby.models.entities.ParticipationRequest;
import com.hobbyFinder.hubby.models.enums.InterestEnum;
import com.hobbyFinder.hubby.models.enums.PrivacyEnum;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventConstants {

    public static final String STREET_EVENT1 = "Street 1";
    public static final String DISTRICT_EVENT1 = "District 1";
    public static final String NUMBER_EVENT1 = "Number 1";
    public static final String CITY_EVENT1 = "City 1";
    public static final String COUNTRY_EVENT1 = "Country 1";

    public static final String STREET_EVENT2 = "Street 2";
    public static final String DISTRICT_EVENT2 = "District 2";
    public static final String NUMBER_EVENT2 = "Number 2";
    public static final String CITY_EVENT2 = "City 2";
    public static final String COUNTRY_EVENT2 = "Country 2";

    public static final String UNUSED_STREET_EVENT = "Street UNUSED";
    public static final String UNUSED_DISTRICT_EVENT = "District UNUSED";
    public static final String UNUSED_NUMBER_EVENT = "Number UNUSED";
    public static final String UNUSED_CITY_EVENT = "City UNUSED";
    public static final String UNUSED_COUNTRY_EVENT = "Country UNUSED";

    public static final String NAME_EVENT = "Futebol";
    public static final LocalDateTime DATE_TIME_EVENT_BEGIN = LocalDateTime.now().plusMonths(1);
    public static final LocalDateTime DATE_TIME_EVENT_END = LocalDateTime.now().plusMonths(2);
    public static final PrivacyEnum PRIVACY_ENUM1 = PrivacyEnum.PUBLIC;
    public static final String DESCRIPTION = "Description1";
    public static final List<ParticipationRequest> PARTICIPATION_REQUEST_LIST = new ArrayList<>();
    public static final int MAX_USER_AMOUNT = 5;
    public static final List<Participation> LOCAL_LIST = new ArrayList<>();

    public static final String NAME_EVENT2 = "Volei";
    public static final LocalDateTime DATE_TIME_EVENT_BEGIN2 = LocalDateTime.now().plusMonths(1);
    public static final LocalDateTime DATE_TIME_EVENT_END2 = LocalDateTime.now().plusMonths(2);
    public static final PrivacyEnum PRIVACY_ENUM2 = PrivacyEnum.PUBLIC;
    public static final String DESCRIPTION2 = "Description1";
    public static final List<ParticipationRequest> PARTICIPATION_REQUEST_LIST2 = new ArrayList<>();
    public static final int MAX_USER_AMOUNT2 = 5;
    public static final List<Participation> LOCAL_LIST2 = new ArrayList<>();

    public static final String UNUSED_NAME_EVENT = "Basquete";
    public static final LocalDateTime UNUSED_DATE_TIME_EVENT_BEGIN = LocalDateTime.now().plusMonths(1);
    public static final LocalDateTime UNUSED_DATE_TIME_EVENT_END = LocalDateTime.now().plusMonths(2);
    public static final PrivacyEnum UNUSED_PRIVACY_ENUM = PrivacyEnum.PUBLIC;
    public static final String UNUSED_DESCRIPTION = "Description1";
    public static final List<ParticipationRequest> UNUSED_PARTICIPATION_REQUEST_LIST = new ArrayList<>();
    public static final int UNUSED_MAX_USER_AMOUNT = 5;
    public static final List<Participation> UNUSED_LOCAL_LIST = new ArrayList<>();

    public static final LocalDto LOCAL_DTO = new LocalDto(STREET_EVENT1, DISTRICT_EVENT1, NUMBER_EVENT1, CITY_EVENT1, COUNTRY_EVENT1, 40, 60);
    public static final EventCreateDto EVENT_CREATE_DTO = new EventCreateDto(NAME_EVENT, DATE_TIME_EVENT_BEGIN, DATE_TIME_EVENT_END,
            LOCAL_DTO, PRIVACY_ENUM1, DESCRIPTION, MAX_USER_AMOUNT, UserConstants.INTERESSE_USADO);

    public static final LocalDto LOCAL_DTO2 = new LocalDto(STREET_EVENT2, DISTRICT_EVENT2, NUMBER_EVENT2, CITY_EVENT2, COUNTRY_EVENT2, 50, 70);
    public static final EventCreateDto EVENT_CREATE_DTO2 = new EventCreateDto(NAME_EVENT2, DATE_TIME_EVENT_BEGIN2, DATE_TIME_EVENT_END2,
            LOCAL_DTO2, PRIVACY_ENUM2, DESCRIPTION2, MAX_USER_AMOUNT2, UserConstants.INTERESSE_USADO);

    public static final LocalDto UNUSED_LOCAL = new LocalDto(UNUSED_STREET_EVENT, UNUSED_DISTRICT_EVENT, UNUSED_NUMBER_EVENT, UNUSED_CITY_EVENT, UNUSED_COUNTRY_EVENT, 90, 110);

}
