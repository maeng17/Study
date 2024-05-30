package com.example.oauthjwt.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final UserDTO userDTO;
    public CustomOAuth2User(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    //구글과 네이버의 attribute 형태가 다르기 떄문에 따로 생성해서 넘겨준다.
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    //Role 값 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userDTO.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getName() {
        return userDTO.getName();
    }

    public String getUsername() {
        return  userDTO.getUsername();
    }
}
