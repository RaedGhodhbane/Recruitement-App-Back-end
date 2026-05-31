package com.app.recruitmentapp.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginRateLimiter extends OncePerRequestFilter {

    private final Map<String, UserAttempts> attempts = new ConcurrentHashMap<>();

    @Value("${rate-limit.login.max-attempts:10}")
    private int maxAttempts;

    @Value("${rate-limit.login.window-minutes:15}")
    private int windowMinutes;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (isLoginRequest(request)) {
            String ip = getClientIP(request);
            if (isRateLimited(ip)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write(
                    "{\"error\":\"Trop de tentatives de connexion. Réessayez dans " + windowMinutes + " minutes.\"}");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    @Scheduled(fixedRateString = "${rate-limit.login.cleanup-minutes:5}000")
    public void cleanupStaleEntries() {
        Instant now = Instant.now();
        Duration window = Duration.ofMinutes(windowMinutes);
        attempts.entrySet().removeIf(entry -> {
            UserAttempts ua = entry.getValue();
            synchronized (ua) {
                ua.cleanOld(now, window);
                return ua.count() == 0;
            }
        });
    }

    private boolean isLoginRequest(HttpServletRequest request) {
        return request.getRequestURI().equals("/api/auth/login")
                && "POST".equalsIgnoreCase(request.getMethod());
    }

    private String getClientIP(HttpServletRequest request) {
        String xf = request.getHeader("X-Forwarded-For");
        if (xf != null && !xf.isBlank()) {
            return xf.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private boolean isRateLimited(String ip) {
        Instant now = Instant.now();
        UserAttempts userAttempts = attempts.computeIfAbsent(ip, k -> new UserAttempts());
        synchronized (userAttempts) {
            userAttempts.cleanOld(now, Duration.ofMinutes(windowMinutes));
            if (userAttempts.count() >= maxAttempts) {
                return true;
            }
            userAttempts.add(now);
            return false;
        }
    }

    private static class UserAttempts {
        private final LinkedList<Instant> timestamps = new LinkedList<>();

        void add(Instant now) {
            timestamps.addLast(now);
        }

        void cleanOld(Instant now, Duration window) {
            while (!timestamps.isEmpty()
                    && Duration.between(timestamps.getFirst(), now).compareTo(window) > 0) {
                timestamps.removeFirst();
            }
        }

        int count() {
            return timestamps.size();
        }
    }
}
