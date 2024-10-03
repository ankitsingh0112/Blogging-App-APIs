package com.ankit.blog_app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

// this class is used to disable the csrf token and make our own configuration
// if we do this then no login form will come before accessing the page
@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    public static final String[] PUBLIC_URLS = {
            "v3/api-docs",
            "v2/api-docs",
            "swagger-resources/**",
            "swagger-ui/**",
            "webjars/**"
    };

    @Autowired
    private JwtFilter jwtFilter;


    // by using this function we are taking the control of spring security in our own hand
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // disabling csrf token using lambda way : (by doing this we don't need to pass the csrf token for post request bcoz we are making it stateless)
        http.csrf(customizer -> customizer.disable());
        // enabling the login page
        http.authorizeHttpRequests(request -> request
                .requestMatchers("register", "login").permitAll()// this will allow the register method without any login
                .requestMatchers(PUBLIC_URLS).permitAll()
                .requestMatchers(HttpMethod.GET).permitAll()
                .anyRequest().authenticated());
        //http.formLogin(Customizer.withDefaults()); // when we are using stateless session then we don't need any form login
        http.httpBasic(Customizer.withDefaults());
        // now making the session stateless so that every time we refresh the page it will create a new session for us
        // and also adding jwtFilter for the jwt token
        http.sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // for jwt token verification


        return http.build();
    }

    // till now, we have hard coded the username and password in application.properties file but now
    // let's create username and password for multiple users
    // for that first we have to return the object of UserDetailsService
    // lets see how we do this

    // here we are using static value but if we want to use the value from database see the below code
//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails admin = User
//                .withDefaultPasswordEncoder()
//                .username("ankit")
//                .password("1234")
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user = User
//                .withDefaultPasswordEncoder()
//                .username("navin")
//                .password("7890")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }

    // dynamically getting the user and password from database

    @Autowired
    private UserDetailsService userDetailsService;
    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        //provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance()); // this will check the password in plain text
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12)); // this will decrypt the password and match with the database
        return provider;
    }

    // for login credentials
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

