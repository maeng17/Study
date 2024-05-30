package com.example.testsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //암호화 생성
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //권한설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //특정 경로에 대한 인가 작업 진행
        http     //위에서 부터 작용. 순서가 중요함.
                .authorizeHttpRequests((auth) -> auth    //람다식으로 작성
                        .requestMatchers("/", "/login", "/loginProc", "/join", "/joinProc").permitAll() //requestMatchers: 특정경로에 요청 진행 .권한부여
                        .requestMatchers("/admin").hasRole("ADMIN") // "/admin"경로에 "ADMIN" role을 가지고 있으면 접근 가능
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated() //위에서 처리하지 못한 나머지 경로.로그인한 사용자만 접근 가능
                );

        http
                .formLogin((auth) -> auth.loginPage("/login") // 자동 리다이렉션
                        .loginProcessingUrl("/loginProc") //"/login"에 대한 데이터를 특정 경로로 보냄
                        .permitAll()
                );

        //csrf: 위변조 자동 설정 / post요청시 csrf token을 보내주어야 로그인 진행
        //현재 토큰이 없기 때문에 중단시켜놓음
        http
                .csrf((auth) -> auth.disable());

        //다중 로그인 설정
        http
                .sessionManagement((auth) -> auth
                        .maximumSessions(1) //동시접속 중복로그인 수
                        .maxSessionsPreventsLogin(true));
                            //값을 초과했을 경우 true: 새로운 로그인 차단 / false: 기존 세션 삭제 -> 새로운 로그인 진행


        return http.build();
    }
}
