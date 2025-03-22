package com.hobbyFinder.hubby.models.dto.participationRequest;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventRequestResponse(
        UUID id,
        String nome,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        String privacidade,
        int CapacidadeMaxima,
        int QuantosUsuarios) {
}
