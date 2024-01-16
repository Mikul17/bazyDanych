package com.mikul17.bazyDanych.Security;

import com.mikul17.bazyDanych.Security.Config.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter JwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfiguration()))
                .authorizeHttpRequests(request -> request
                                .requestMatchers("/api/auth/signup","/api/auth/login","/api/auth/verify",
                                        "/api/bet/match/**","/api/league/all","/api/league/{id}",
                                        "/api/match/todays","/api/match/tomorrows","/api/match/upcoming",
                                        "/api/match/history/**","/api/match/{id}","/api/matchStat/**",
                                        "/api/matchEvent/**","/api/player/all","/api/player/all/{teamId}",
                                        "/api/player/{id}","/api/player_skill/**","/api/player_stat/**",
                                        "/api/team/all","/api/team/get/{id}","/api/team/getByLeague/{id}",
                                        "/api/team/all/league/order/{leagueId}","/api/user/resetPassword",
                                        "/api/user/resetRequest","/api/player/all/name/{teamName}").permitAll()
                        .requestMatchers("/api/address/{id}","/api/auth/role/{userId}","/api/coupon/all/{userId}",
                                "/api/coupon/add","/api/coupon/won/{userId}"
                                ,"/api/coupon/lost/{userId}","/api/coupon/active/{userId}",
                                "/api/transactions/**","/api/user/{id}","/api/user/balance/{id}",
                                "/api/user/update/**","/api/user/changeAddress","/api/user/changePassword").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/api/address/add","/api/bet/placeBet","/api/bet/update/{id}",
                                        "/api/bet/all","/api/bet/{id},","/api/bet/check/{id}","/api/bet/delete/{id}",
                                        "/api/betType/**","/api/coupon/all","/api/coupon/{couponId}",
                                        "/api/coupon/delete/{couponId}","/api/league/add","/api/league/addLeagues",
                                        "/api/match/add","/api/player/add","/api/player/delete/{id}",
                                        "/api/team/add","/api/team/addTeams","/api/team/reset/{id}",
                                        "/api/transactions/all","/api/user/fullUserInfo/{id}","/api/user/ban",
                                        "/api/user/unban","/api/user/allUsers","/api/user/allUsers/withBlocked").hasRole("ADMIN")

                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(JwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    private static CorsConfigurationSource corsConfiguration () {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000"));
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setMaxAge(3600L);
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "Requestor-Type"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setExposedHeaders(List.of("Authorization","X-Get-Header"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration); // Apply the configuration to all paths
        return source;
    }
}
