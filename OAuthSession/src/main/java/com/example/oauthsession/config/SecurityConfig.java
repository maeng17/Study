package com.example.oauthsession.config;

import com.example.oauthsession.oauth2.CustomClientRegistrationRepo;
import com.example.oauthsession.oauth2.CustomOAuth2AuthorizedClientService;
import com.example.oauthsession.service.CustomOauth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOauth2UserService customOauth2UserService;

    private final CustomClientRegistrationRepo customClientRegistrationRepo;

    private final CustomOAuth2AuthorizedClientService customOAuth2AuthorizedClientService;
    private final JdbcTemplate jdbcTemplate;

    public SecurityConfig(
            CustomOauth2UserService customOauth2UserService,
            CustomClientRegistrationRepo customClientRegistrationRepo,
            CustomOAuth2AuthorizedClientService customOAuth2AuthorizedClientService, JdbcTemplate jdbcTemplate) {

        this.customOauth2UserService = customOauth2UserService;
        this.customClientRegistrationRepo = customClientRegistrationRepo;
        this.customOAuth2AuthorizedClientService = customOAuth2AuthorizedClientService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf((csrf) -> csrf.disable());

        http
                .formLogin((login) -> login.disable());

        http
                .httpBasic((basic) -> basic.disable());

        //OAuth2 login
        http
                .oauth2Login((oauth2) -> oauth2
                        .loginPage("/login") //커스텀 로그인 페이지 등록
                        .clientRegistrationRepository(customClientRegistrationRepo.clientRegistrationRepository()) //customRegistration 등록
                        //JDBC 연결
                        .authorizedClientService(customOAuth2AuthorizedClientService.oAuth2AuthorizedClientService(jdbcTemplate, customClientRegistrationRepo.clientRegistrationRepository()))
                        .userInfoEndpoint((userInfoEndpointConfig) ->
                                userInfoEndpointConfig.userService(customOauth2UserService)));

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/oauth2/**", "/login/**").permitAll()
                        .anyRequest().authenticated());

        return http.build();
    }
}
