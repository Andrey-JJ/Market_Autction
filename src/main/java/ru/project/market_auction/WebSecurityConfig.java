package ru.project.market_auction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.project.market_auction.services.auth.AuthUserDetailsService;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    @Autowired
    private AuthUserDetailsService authUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests ->
                    authorizeRequests
                            .requestMatchers("/registration", "/login", "/").permitAll()
                            .requestMatchers("/market/**", "/auction/**").permitAll()
                            //Для реурсов из папки static
                            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                            .anyRequest().permitAll())
            .formLogin(login ->
                    login.loginPage("/login").permitAll())
            .logout(logout -> logout.permitAll());
        return http.build();
    }
}
