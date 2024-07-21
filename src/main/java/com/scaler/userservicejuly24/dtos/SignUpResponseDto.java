package com.scaler.userservicejuly24.dtos;

import com.scaler.userservicejuly24.models.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpResponseDto {
    private User user;
    private ResponseStatus responseStatus;
}
