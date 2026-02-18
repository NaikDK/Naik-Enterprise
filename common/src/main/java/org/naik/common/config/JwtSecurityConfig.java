package org.naik.common.config;

import org.naik.common.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity
public class JwtSecurityConfig {
    @SuppressWarnings("unused")
    private final JwtAuthenticationFilter jwtAuthFilter;

    public JwtSecurityConfig(JwtAuthenticationFilter jwtAuthFilter){
        this.jwtAuthFilter = jwtAuthFilter;
    }

    // @Bean(name="jwtFilterChain")
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
    //     http
    //         .csrf(csrf -> csrf.disable())
    //         .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    //         .authorizeHttpRequests(auth -> auth
    //             .requestMatchers("/**/health").permitAll()
    //             .requestMatchers("/*/actuator/**").permitAll()
    //             .anyRequest().authenticated()
    //         )
    //         .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    //         return http.build();
    // }
}
