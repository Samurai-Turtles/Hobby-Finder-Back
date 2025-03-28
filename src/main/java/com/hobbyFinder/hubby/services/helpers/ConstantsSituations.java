package com.hobbyFinder.hubby.services.helpers;

import com.hobbyFinder.hubby.models.dto.situations.SituationDto;

public class ConstantsSituations {

    public static final SituationDto CONFIRMED = new SituationDto("PARTICIPANTE");
    public static final SituationDto NOT_CONFIRMED = new SituationDto("AGUARDANDO_CONFIRMAÇÃO");
    public static final SituationDto NOT_ACCEPTED = new SituationDto("AGUARDANDO_SOLICITAÇÃO");
    public static final SituationDto NOT_THERE = new SituationDto("NÃO_PARTICIPANTE");

}
