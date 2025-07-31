package br.com.biblioteca.biblioteca_api.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private static final String[] PUBLIC_MATCHERS_POST = {
            "/usuarios"
    };

    private final UserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;

    public WebSecurityConfig(UserDetailsService userDetailsService, JWTUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/h2-console/**",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-ui.html"
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
                        .requestMatchers(HttpMethod.POST, "/autores", "/categorias", "/livros").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/autores/**", "/categorias/**", "/livros/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/autores/**", "/categorias/**", "/livros/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/autores/**", "/categorias/**", "/livros/**").authenticated()
                        .requestMatchers("/emprestimos/**").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilter(new JWTAuthenticationFilter(authenticationManager, jwtUtil))
                .addFilter(new JWTAuthorizationFilter(authenticationManager, jwtUtil, userDetailsService));

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
        config.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "PATCH", "OPTIONS"));
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}