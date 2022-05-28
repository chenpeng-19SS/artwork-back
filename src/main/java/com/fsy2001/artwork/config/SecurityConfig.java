package com.fsy2001.artwork.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsy2001.artwork.model.User;
import com.fsy2001.artwork.repository.UserRepository;
import com.fsy2001.artwork.security.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;
    private final UserRepository repository;
    private final ObjectMapper objectMapper;


    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder,
                          ApplicationUserService applicationUserService, UserRepository repository, ObjectMapper objectMapper) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/css/**", "/js/**", "/plugins/**", "/images/**", "/fonts/**").permitAll()
                .and()
                .formLogin().loginProcessingUrl("/api/login")
                .successHandler((request, response, auth) -> { // when success login, return user info
                    User loginUser = repository.getUserByUsername(auth.getName());
                    loginUser.setPassword("");

                    Map<String, Object> map = new HashMap<>();
                    map.put("code", 200);
                    map.put("message", "success");
                    map.put("user", loginUser);
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    out.write(objectMapper.writeValueAsString(map));
                    out.flush();
                    out.close();
                })
                .failureHandler((request, response, ex) -> { // when login failed, return error message
                    response.setContentType("application/json;charset=utf-8");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    PrintWriter out = response.getWriter();
                    Map<String, Object> map = new HashMap<>();
                    map.put("httpStatus", 401);
                    // put message in different cases
                    if (ex instanceof UsernameNotFoundException
                            || ex instanceof BadCredentialsException) {
                        map.put("message", "login-fail");
                    } else if (ex instanceof DisabledException) {
                        map.put("message", "account-banned");
                    } else {
                        map.put("message", "unknown-error");
                    }
                    out.write(objectMapper.writeValueAsString(map));
                    out.flush();
                    out.close();
                })
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    // return error message when access resources that require login
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    Map<String, Object> map = new HashMap<>();
                    map.put("httpStatus", 401);
                    map.put("message", "require-login");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    out.write(objectMapper.writeValueAsString(map));
                    out.flush();
                    out.close();
                })
                .and()
                .rememberMe()
                .and()
                .logout()
                .logoutUrl("/api/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .logoutSuccessUrl("/index");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
        return provider;
    }
}
