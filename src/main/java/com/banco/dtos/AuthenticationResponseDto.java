package com.banco.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponseDto {

    private String token;
    private String refresh_token;
}
