package tech.rwong.sysu.cloud.server.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        this.logger.info("Request received: " + request.getRemoteAddr());
        this.logger.info(request.getContentType());
        this.logger.info(request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)
        );
        //attempt Authentication when Content-Type is json
        if(request.getContentType().startsWith(MediaType.APPLICATION_JSON_VALUE)){

            //use jackson to deserialize json
            ObjectMapper mapper = new ObjectMapper();
            UsernamePasswordAuthenticationToken authRequest = null;
            try (InputStream is = request.getInputStream()){
                AuthenticationBean authenticationBean = mapper.readValue(is, AuthenticationBean.class);
                this.logger.info(authenticationBean.getUsername());
                this.logger.info(authenticationBean.getPassword());
                authRequest = new UsernamePasswordAuthenticationToken(
                        authenticationBean.getUsername(), authenticationBean.getPassword());
            }catch (IOException e) {
                e.printStackTrace();
                authRequest = new UsernamePasswordAuthenticationToken("", "");
            }finally {
                setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            }
        }

        //transmit it to UsernamePasswordAuthenticationFilter
        else {
            return super.attemptAuthentication(request, response);
        }
    }
}
