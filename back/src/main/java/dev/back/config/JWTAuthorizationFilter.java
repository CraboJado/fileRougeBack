package dev.back.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class JWTAuthorizationFilter extends OncePerRequestFilter {
    private JWTConfig jwtConfig;

    public JWTAuthorizationFilter(JWTConfig jwtConfig){
        this.jwtConfig = jwtConfig;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        if(req.getCookies() != null) {
            Stream.of(req.getCookies())
                    .filter( cookie -> cookie.getName().equals(jwtConfig.getCookie()))
                    .map( Cookie::getValue)
                    .forEach(token -> {
                        Claims body = Jwts
                                .parserBuilder()
                                .setSigningKey(jwtConfig.getSecretKey())
                                .build()
                                .parseClaimsJws(token)
                                .getBody();

                        String email = body.getSubject();
//                        List<String> roles = body.get("roles", List.class);
                        String role = body.get("roles", String.class);
//                        System.out.println("roles=====================================ADMIN="+ role);
                        //                        {sub=abc@hotmail.fr, roles=ADMIN, exp=1690576791}
                        List<String> roles = new ArrayList<>();
                        roles.add(role);

                        List<SimpleGrantedAuthority> authorities = roles
                                .stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList());
//                       System.out.println("authorities====================================[ADMIN]="+ authorities);
                        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);

//                       System.out.println("authentication====================================="+ authentication);
//                      UsernamePasswordAuthenticationToken
//                      [Principal=abc@hotmail.fr, Credentials=[PROTECTED], Authenticated=true, Details=null, Granted Authorities=[ADMIN]]
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    });
        }

        chain.doFilter(req,res);
    }
}
