package com.revature.revado.security;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull FilterChain filterChain)
            throws jakarta.servlet.ServletException, java.io.IOException {

        //1. Extract the jwt token from header
        String authHeader = request.getHeader("Authorization");
        String token;
        //2. Check if it's a "Bearer" token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                String username = jwtUtils.extractUsername(token);
                //Check if the username is valid and the user is not already authenticated in the current session
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    //Package the user information passing the (userdetails, null for password because we trust the jwt token, the user role)
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    //Capture the request information such as IP address and the session id
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    //Take the authToken we just built and saves it into the security context.
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception e) {
                logger.error("Could not set user authentication ", e);
            }
        }

        // Always continue the filter chain so the request reaches the actual endpoint
        filterChain.doFilter(request, response);
    }
}
