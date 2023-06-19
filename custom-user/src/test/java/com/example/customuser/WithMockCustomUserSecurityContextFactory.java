package com.example.customuser;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Optional;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser mockCustomUser) {
        var username = mockCustomUser.email();
        CustomUserRepository userRepository = email -> Optional.of(new CustomUser(mockCustomUser.id(), username, ""));
        var userDetailsService = new CustomUserRepositoryUserDetailsService(userRepository);
        var userDetails = userDetailsService.loadUserByUsername(username);
        var securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));
        return securityContext;
    }
}
