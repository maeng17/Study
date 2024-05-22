package com.example.oauthsession.service;

import com.example.oauthsession.dto.CustomOAuth2User;
import com.example.oauthsession.dto.GoogleResponse;
import com.example.oauthsession.dto.NaverResponse;
import com.example.oauthsession.dto.OAuth2Response;
import com.example.oauthsession.entity.UserEntity;
import com.example.oauthsession.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService { //COauth2UserService의 구현체

    private final UserRepository userRepository;

    public CustomOauth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //사용자 정보 데이터를 인자로 받아옴 -> naver, google모두 가져올 수 있음
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest); //유저 정보 가져오기
        System.out.println(oAuth2User.getAttributes());

        //google, naver 인지 구분 -> 인증 데이터의 규격의 서로 다르기 떄문
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;


        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());

        } else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        //구현
        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        //네이버인지 구글인지 구분 + " " + userId

        UserEntity existData = userRepository.findByUsername(username);

        String role = "ROLE_USER";
        if(existData == null) { //처음 로그인하는 경우

            UserEntity userEntity = new UserEntity();

            userEntity.setUsername(username);
            userEntity.setEmail(oAuth2Response.getEmail());
            userEntity.setRole(role);

            userRepository.save(userEntity); //저장

        } else { //이미 존재하는 경우

            existData.setUsername(username);
            existData.setEmail(oAuth2Response.getEmail());

            role = existData.getRole();

            userRepository.save(existData);

        }

        return new CustomOAuth2User(oAuth2Response, role);
    }


}
