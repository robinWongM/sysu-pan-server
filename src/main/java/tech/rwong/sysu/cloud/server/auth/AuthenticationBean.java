package tech.rwong.sysu.cloud.server.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationBean {
    private String username;
    private String password;
}