package com.scaler.userservicejuly24.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaler.userservicejuly24.dtos.SendEmailEventDto;
import com.scaler.userservicejuly24.models.Token;
import com.scaler.userservicejuly24.models.User;
import com.scaler.userservicejuly24.repositories.TokenRepository;
import com.scaler.userservicejuly24.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    protected BCryptPasswordEncoder bCryptPasswordEncoder;
    private TokenRepository tokenRepository;
    private KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           TokenRepository tokenRepository,
                           KafkaTemplate kafkaTemplate,
                           ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public User signUp(String name, String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = null;

        if (optionalUser.isPresent()) {
            //Navigate them to Login
        } else {
            //Create a new User object.
            user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setHashedPassword(bCryptPasswordEncoder.encode(password));

            user = userRepository.save(user);

            //Publish an event inside the Queue.
//            SendEmailEventDto emailEventDto = new SendEmailEventDto();
//            emailEventDto.setTo(email);
//            emailEventDto.setFrom("t18696872@gmail.com");
//            emailEventDto.setSubject("Welcome to Scaler");
//            emailEventDto.setBody("Welcome to Scaler, We are very happy to have you on our platform. All the best!!");
//
//            try {
//                kafkaTemplate.send(
//                        "sendEmail",
//                        objectMapper.writeValueAsString(emailEventDto)
//                );
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
        }

        return user;
    }

    @Override
    public Token login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = null;

        if (optionalUser.isEmpty()) {
            //signup method.
        } else {
            user = optionalUser.get();

            if (!bCryptPasswordEncoder.matches(password, user.getHashedPassword())) {
                return null;
            }

            //Generate the token.
            Token token = createToken(user);
            token = tokenRepository.save(token);

            return token;
        }

        return null;
    }

    private Token createToken(User user) {
        Token token = new Token();
        token.setUser(user);
        token.setValue(RandomStringUtils.randomAlphanumeric(128));

        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysLater = today.plus(30, ChronoUnit.DAYS);

        Date expiryAt = Date.from(thirtyDaysLater.atStartOfDay(ZoneId.systemDefault()).toInstant());

        token.setExpiryAt(expiryAt);

        return token;
    }

    @Override
    public User validateToken(String token) {
        Optional<Token> tokenOptional = tokenRepository.findByValueAndDeletedAndExpiryAtGreaterThan(
                token,
                false,
                new Date()
        );

        if (tokenOptional.isEmpty()) {
            //Throw some exception
            return null;
        }

        return tokenOptional.get().getUser();
    }

    @Override
    public void logout(String token) {
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeleted(token, false);

        if (optionalToken.isEmpty()) {
            //Throw some exception
        }

        Token token1 = optionalToken.get();

        token1.setDeleted(true);
        tokenRepository.save(token1);
    }

    @Override
    public User getUserDetails(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User with id :" + userId + " not found");
        }

        return optionalUser.get();
    }
}
