package org.musicplatform.auth.service.refreshToken;

import org.musicplatform.auth.repository.RefreshTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
public class RefreshTokenCleanUpTask {

    private final RefreshTokenRepository repository;

    public RefreshTokenCleanUpTask(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void clearExpiredTokens(){
        repository.deleteAllByExpiryDateBefore(Instant.now());
    }
}
