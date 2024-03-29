package com.peekaboo.security.jwt;

import com.peekaboo.model.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final Logger logger = LogManager.getLogger(JwtAuthenticationProvider.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {}

    @Override
    protected UserDetails retrieveUser(String s, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        String token = jwtAuthenticationToken.getToken();

        logger.error("Token is:");
        logger.error(token);
        logger.debug("Retrieving user from token");
        User parsedUser = jwtUtil.parse(token);
        logger.debug("HERE IS USER");
        logger.debug(parsedUser);


        if (parsedUser == null) {
            logger.debug("Error with retrieving user. Warn: authorization as anonymous user without any role");
            //todo: find better way
            return new org.springframework.security.core.userdetails.User("anonymous", "anonymous", new ArrayList<GrantedAuthority>());
        }

        return parsedUser;
    }
}
