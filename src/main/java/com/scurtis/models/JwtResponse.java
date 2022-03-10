package com.scurtis.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class JwtResponse {
    @JsonProperty("access_token")
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private Long id;
    private List<String> roles;
}
