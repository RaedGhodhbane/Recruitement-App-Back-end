package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.BlacklistedToken;
import com.app.recruitmentapp.repositories.BlacklistedTokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TokenBlacklistServiceImpl Unit Tests")
class TokenBlacklistServiceImplTest {

    @Mock
    private BlacklistedTokenRepository blacklistedTokenRepository;
    @InjectMocks
    private TokenBlacklistServiceImpl tokenBlacklistService;

    @Captor
    private ArgumentCaptor<BlacklistedToken> tokenCaptor;

    @Nested
    @DisplayName("blacklistToken Tests")
    class BlacklistTokenTests {

        @Test
        @DisplayName("Should blacklist token successfully")
        void shouldBlacklistTokenSuccessfully() {
            String token = "test-jwt-token-123";
            LocalDateTime expirationTime = LocalDateTime.now().plusHours(1);

            when(blacklistedTokenRepository.save(any(BlacklistedToken.class))).thenAnswer(
                    invocation -> invocation.getArgument(0));

            tokenBlacklistService.blacklistToken(token, expirationTime);

            verify(blacklistedTokenRepository).save(tokenCaptor.capture());
            BlacklistedToken savedToken = tokenCaptor.getValue();

            assertEquals(token, savedToken.getToken());
            assertEquals(expirationTime, savedToken.getExpiresAt());
            assertNotNull(savedToken.getBlacklistedAt());
        }
    }

    @Nested
    @DisplayName("isTokenBlacklisted Tests")
    class IsTokenBlacklistedTests {

        @Test
        @DisplayName("Should return true when token is blacklisted")
        void shouldReturnTrueWhenTokenIsBlacklisted() {
            String token = "blacklisted-jwt-token";
            when(blacklistedTokenRepository.existsByToken(token)).thenReturn(true);

            boolean result = tokenBlacklistService.isTokenBlacklisted(token);

            assertTrue(result);
            verify(blacklistedTokenRepository).existsByToken(token);
        }

        @Test
        @DisplayName("Should return false when token is not blacklisted")
        void shouldReturnFalseWhenTokenIsNotBlacklisted() {
            String token = "valid-jwt-token";
            when(blacklistedTokenRepository.existsByToken(token)).thenReturn(false);

            boolean result = tokenBlacklistService.isTokenBlacklisted(token);

            assertFalse(result);
            verify(blacklistedTokenRepository).existsByToken(token);
        }
    }
}
