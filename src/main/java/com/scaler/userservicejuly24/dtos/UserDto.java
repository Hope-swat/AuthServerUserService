package com.scaler.userservicejuly24.dtos;

import com.scaler.userservicejuly24.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDto {
    private String name;
    private String email;
    private List<Role> roles;
}
