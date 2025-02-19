package com.hobbyFinder.hubby.exception.AuthException;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.hobbyFinder.hubby.exception.HubbyException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthErrorType {

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
    @JsonProperty("message")
    private String message;
    @JsonProperty("erros")
    private List<String> errors;

    public AuthErrorType(HubbyException e) {
        this.timestamp = LocalDateTime.now();
        this.message = e.getMessage();
        this.errors = new ArrayList<>();
    }
}
