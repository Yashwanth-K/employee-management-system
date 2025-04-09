package com.yash.employeemanagement.security;

import com.yash.employeemanagement.exception.JwtTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.webresources.JarWarResourceSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Registers this class as a Spring bean so it can be autowired elsewhere
public class JwtFilter extends OncePerRequestFilter {
    // Extends OncePerRequestFilter to guarantee this filter is executed only once per request

    @Autowired
    private JwtUtil jwtUtil; // Utility class for JWT operations (generation, validation, parsing)

    @Autowired
    private UserDetailsService userDetailsService; // Service to load user details from the database

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        // This method is called for every request to the application

        // Bypass JWT filter for paths starting with "/auth/"
        if (request.getServletPath().startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }
        String authHeader = request.getHeader("Authorization"); // Extract the Authorization header
        String token = null;
        String username = null;

        // Authorization header format: "Bearer <token>"
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Remove "Bearer " prefix to get just the token
            username = jwtUtil.extractUsername(token); // Extract username from the token
            }


        // Only proceed if we found a username and the user isn't already authenticated,
//         this check prevent re-authentication by checking the security context

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load user details from the database using the username from the token
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validate that the token is valid for this user
            if (!jwtUtil.validateToken(token, userDetails.getUsername())) {
                throw new JwtTokenException("Invalid JWT token");
                }
                // Create an authentication object with the user's details and authorities (roles)
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Set the authentication in the SecurityContext
                // This tells Spring Security that the request is authenticated
                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
        }

        // Continue the filter chain to process the request further
        // This allows the request to reach the controller if authenticated
        filterChain.doFilter(request, response);
    }
}