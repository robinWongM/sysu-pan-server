package tech.rwong.sysu.cloud.server.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInMessageBean {
    private String err;

    public SignInMessageBean(String err) {
        this.err = err;
    }
}