package com.movieflix.auth.services;

import com.movieflix.auth.entities.RefreshToken;
import com.movieflix.auth.entities.User;
import com.movieflix.auth.repositories.RefreshTokenRepository;
import com.movieflix.auth.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService (UserRepository userRepository,RefreshTokenRepository refreshTokenRepositor ){
        this.refreshTokenRepository=refreshTokenRepositor;
        this.userRepository=userRepository;
    }

    public RefreshToken CreateRefreshToke(String username){
        User user = userRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User not Found with user id : " + username));
        RefreshToken refreshToken= user.getRefreshToken();

        if (refreshToken == null){
            long expiryTime = 5*60*60*10000;
            refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expirationTime(Instant.now().plusMillis(expiryTime))
                    .user(user)
                    .build();
            refreshTokenRepository.save(refreshToken);
        }
        return refreshToken;
    }

    public RefreshToken VerifyRefreshToken(String refreshToken){
        RefreshToken refToken = refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(()-> new RuntimeException("Refresh token not found"));
        if (refToken.getExpirationTime().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(refToken);
            throw new RuntimeException("Refresh Token Expired ");
        }
        return refToken;
    }
}
