package com.app.recruitmentapp.services;

import java.time.LocalDateTime;

public interface TokenBlacklistService {
    void blacklistToken(String token, LocalDateTime expirationTime);
    boolean isTokenBlacklisted(String token);
}
