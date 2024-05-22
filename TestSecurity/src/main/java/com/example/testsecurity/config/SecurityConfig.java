package com.example.testsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //암호화 생성
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public RoleHierarchy hierarchy() {
//        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
//        hierarchy.setHierarchy("ROLE_C > ROLE_B\n" +
//                "ROLE_B > ROLE_A");
//
//        return hierarchy;
//    }

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

//        http     //위에서 부터 작용. 순서가 중요함.
//                .authorizeHttpRequests((auth) -> auth    //람다식으로 작성
//                        .requestMatchers("/login", "/loginProc", "/join", "/joinProc").permitAll() //requestMatchers: 특정경로에 요청 진행 .권한부여
//                        .requestMatchers("/").hasAnyRole("A")
//                        .requestMatchers("/manager").hasAnyRole("B")
//                        .requestMatchers("/admin").hasRole("C") // "/admin"경로에 "ADMIN" role을 가지고 있으면 접근 가능
//                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
//                        .anyRequest().authenticated() //위에서 처리하지 못한 나머지 경로.로그인한 사용자만 접근 가능
//                );

        //formLogin 로그인 방식
        http
                .formLogin((auth) -> auth.loginPage("/login") // 자동 리다이렉션
                        .loginProcessingUrl("/loginProc") //"/login"에 대한 데이터를 특정 경로로 보냄
                        .permitAll()
                );

        //httpBasic 로그인방식 - alert창으로 로그인 페이지 자동생성
//        http
//                .httpBasic(Customizer.withDefaults());


        //csrf: 위변조 자동 설정 / 요청시 csrf token을 받아야 진행
//        http
//                .csrf((auth) -> auth.disable());

        //다중 로그인 설정
        http
                .sessionManagement((auth) -> auth
                        .maximumSessions(1) //동시접속 중복로그인 수
                        .maxSessionsPreventsLogin(true));
                            //값을 초과했을 경우 true: 새로운 로그인 차단 / false: 기존 세션 삭제 -> 새로운 로그인 진행

        //세션 고정 보호
        http
                .sessionManagement((auth) -> auth
                        .sessionFixation().changeSessionId());
                        //none(): 로그인 시 세션 정보 변경 안함 -> 보안상 위험
                        // newSession() : 로그인 시 세션 새로 생성
                        //changeSessionId() : 로그인 시 동일한 세션에 대한 id 변경 -> 세션 고정 보호 가능. 주로 이용

        //로그아웃 - csrf enable시 get 요청으로 할 경우 따로 설정해주어야 함
        http
                .logout((auth) -> auth.logoutUrl("/logout")
                        .logoutSuccessUrl("/"));

        return http.build();
    }

    //InMemory 방식 진행 시 error 남,,
//    @Bean
//    public UserDetailsService userDetailsService() {
//
//        UserDetails user1 = User.builder()
//                .username("user1")
//                .password(bCryptPasswordEncoder().encode("1234"))
//                .roles("C")
//                .build();
//
//        UserDetails user2 = User.builder()
//                .username("user2")
//                .password(bCryptPasswordEncoder().encode("1234"))
//                .roles("A")
//                .build();
//
//        return new InMemoryUserDetailsManager(user1, user2);
//    }


}
