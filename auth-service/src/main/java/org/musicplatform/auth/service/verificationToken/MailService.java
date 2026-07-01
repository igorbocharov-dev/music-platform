package org.musicplatform.auth.service.verificationToken;

public interface MailService {
    void sendActivationEmail(String email, String activationLink);
}
