package com.example.oauthsession.oauth2;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

@Configuration
public class CustomClientRegistrationRepo {

    private final SocialClientRegistration socialClientRegistration;

    public CustomClientRegistrationRepo(SocialClientRegistration socialClientRegistration) {
        this.socialClientRegistration = socialClientRegistration;
    }

    public ClientRegistrationRepository clientRegistrationRepository() {
        //값이 많이 없기 떄문에 Inmemory방식으로 진행
        return new InMemoryClientRegistrationRepository(
                socialClientRegistration.naverClientRegistration(), socialClientRegistration.googleClientRegistration()
        );
    }
}
