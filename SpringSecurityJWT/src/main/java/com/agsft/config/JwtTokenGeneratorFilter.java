package com.agsft.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class JwtTokenGeneratorFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        System.out.println("inside doFilter of JWT generator");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null){
//            System.out.println("Authorities : "+ auth.getAuthorities());
            SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes());

            String jwt = Jwts.builder()
                    .setIssuer("Ram")
                    .setSubject("JWT Token")
                    .claim("username", auth.getName())
                    .claim("authorities", populateAuthorities(auth.getAuthorities()))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(new Date().getTime() + 30000000))
                    .signWith(key).compact();

            response.setHeader(SecurityConstants.JWT_HEADER,jwt);

            Cookie jwtCookie = new Cookie(SecurityConstants.JWT_HEADER, jwt); // Replace jwt with your actual JWT token
            jwtCookie.setMaxAge((int) (System.currentTimeMillis() + 30000000)); // Set cookie expiration time
            jwtCookie.setPath("/");

            response.addCookie(jwtCookie);
        }
        filterChain.doFilter(request,response);
    }

    public String populateAuthorities(Collection<? extends GrantedAuthority> collections){
        Set<String> authoritiesSet = new HashSet<>();

        for(GrantedAuthority authority: collections){
            authoritiesSet.add(authority.getAuthority());
        }

        return String.join(",", authoritiesSet);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        return !request.getServletPath().equals("/signIn");
    }
}
