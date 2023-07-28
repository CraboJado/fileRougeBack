package dev.back.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;

import java.util.Map;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JWTAuthorizationFilter jwtFilter, JWTConfig jwtConfig) throws Exception {

        http.authorizeHttpRequests(
                        auth -> auth
//                        .anyRequest().permitAll()
//                                .requestMatchers(antMatcher("/employe")).permitAll()
                                // route pour ajouter employe, ici ça passe meme non authenticated
//                                .requestMatchers(antMatcher(HttpMethod.POST,"/employe")).permitAll()
                                // pour tester d'abord
//                                .requestMatchers(antMatcher(HttpMethod.POST,"/absence")).permitAll()
                                // session = logique login
                                .requestMatchers(antMatcher(HttpMethod.POST,"/sessions")).permitAll()
//                                .requestMatchers(antMatcher(HttpMethod.POST,"/login")).permitAll()
//                                .requestMatchers(antMatcher("/admin/**")).hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .csrf( csrf -> csrf.disable()
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                        .csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler()::handle)
//                        .ignoringRequestMatchers(PathRequest.toH2Console())
                )
                .headers( headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .logout( logout -> logout
                        .logoutSuccessHandler(((req, resp, auth) -> resp.setStatus(HttpStatus.OK.value())))
                        .deleteCookies(jwtConfig.getCookie())
                );

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        String encodingId = "bcrypt";
        return new DelegatingPasswordEncoder(encodingId, Map.of(encodingId, new BCryptPasswordEncoder()));
    }
}
