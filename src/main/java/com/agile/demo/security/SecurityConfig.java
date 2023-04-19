package com.agile.demo.security;

import com.agile.demo.jwt.CustomAuthenticationEntryPoint;
//import com.agile.demo.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

        private final AuthenticationManagerConfig authenticationManagerConfig;
        private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .formLogin().disable() // 직접 id, password를 입력받아서 JWT토큰을 리턴하는 API를 직접 만든다.
                    .csrf().disable() // CSRF는 Cross Site Request Forgery의 약자. CSRF공격을 막기 위한 방법.
                    .cors() //.configurationSource(corsConfigurationSource())
                    .and()
                    .apply(authenticationManagerConfig)
                    .and()
                    .httpBasic().disable()
                    .authorizeRequests()
                    .requestMatchers(CorsUtils::isPreFlightRequest).permitAll() // Preflight 요청은 허용한다. https://velog.io/@jijang/%EC%82%AC%EC%A0%84-%EC%9A%94%EC%B2%AD-Preflight-request
//                    .mvcMatchers( "/signup", "/login", "/users/refresh").permitAll()
////                .mvcMatchers(GET, "/**").permitAll()
//                    .mvcMatchers(GET,"/**").hasAnyRole("USER", "MANAGER", "ADMIN")
//                    .mvcMatchers(POST,"/**").hasAnyRole("USER", "MANAGER", "ADMIN")
////                .mvcMatchers(POST,"answers/**").hasAnyRole("USER", "MANAGER", "ADMIN")
                    .anyRequest().hasAnyRole()
                    .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(customAuthenticationEntryPoint)
                    .and()
                    .build();
            return null;
        }

        // <<Advanced>> Security Cors로 변경 시도
        public CorsConfigurationSource corsConfigurationSource() {
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            CorsConfiguration config = new CorsConfiguration();
            config.addAllowedOrigin("*");
            config.addAllowedMethod("*");
            config.setAllowedMethods(List.of("GET","POST","DELETE","PATCH","OPTION","PUT"));
            source.registerCorsConfiguration("/**", config);

            return source;
    }
}

