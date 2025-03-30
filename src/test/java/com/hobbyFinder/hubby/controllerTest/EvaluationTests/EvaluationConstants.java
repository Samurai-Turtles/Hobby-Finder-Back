package com.hobbyFinder.hubby.controllerTest.EvaluationTests;

import com.hobbyFinder.hubby.models.dto.evaluations.PostEvaluationDto;

public class EvaluationConstants {

    public static final int FIVE_STARS = 5;
    public static final int INVALID_STARS_LESS = -1;
    public static final int INVALID_STARS_PLUS = 6;

    public static final String COMMENT = "comentario";

    public static final PostEvaluationDto EVALUATION_CREATE_DTO = new PostEvaluationDto(5, COMMENT);
}