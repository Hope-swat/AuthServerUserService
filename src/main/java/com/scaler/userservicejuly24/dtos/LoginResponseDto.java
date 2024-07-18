package com.scaler.userservicejuly24.dtos;

import com.scaler.userservicejuly24.models.Token;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {
    private Token token;
}
