package com.scaler.userservicejuly24.services;

import com.scaler.userservicejuly24.models.Token;
import com.scaler.userservicejuly24.models.User;

public interface UserService {
    User signUp(String name, String email, String password);

    Token login(String email, String password);

    User validateToken(String token);

    void logout(String token);
}
