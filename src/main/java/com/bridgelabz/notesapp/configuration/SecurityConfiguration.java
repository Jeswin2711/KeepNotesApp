package com.bridgelabz.notesapp.configuration;

import com.bridgelabz.notesapp.utility.security.SecurityFilters;
import com.bridgelabz.notesapp.utility.security.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserCredentials userCredentials;

    @Autowired
    private SecurityFilters securityFilters;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userCredentials);
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/register", "/user/login", "/user/confirm-email/**", "/user/forgot-password/{email}",
                        "/user/{user_id}/getnotes", "/user/{username}", "/user/{user_id}/delete/{note_id}",
                        "/user/{user_id}/getarchieved", "/user/{user_id}/getdeleted",
                        "/user/archieve/{username}/{note_id}", "/user/addnote/{id}",
                        "/user/{user_id}/restore/{note_id}", "/user/{user_id}/remove/{note_id}",
                        "/user/{username}/update/{note_id}", "/user/pin/{id}", "/user/{user_id}/getpinned",
                        "/user/send-otp/{email}")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(securityFilters, UsernamePasswordAuthenticationFilter.class);
    }
}
