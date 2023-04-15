package org.tms.travel_agency.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests((auth)->{
            try {
                auth.antMatchers("/allDestinations","/user/registration").permitAll()
                        .antMatchers("/destinationManager").hasRole("ADMIN")
                        .antMatchers("user/accountManager","/user/update").authenticated()
                        .and()
                        .formLogin()
                        .loginPage("/user/login")
                        .loginProcessingUrl("/try-login")
                        .successHandler((request, response, authentication) -> {
                            response.sendRedirect("/user/accountManager");
                                })
                        .and()
                        .logout()
                        .logoutUrl("/try-logout")
                        .addLogoutHandler((request, response, authentication) ->{

                                    try {
                                        response.sendRedirect("/user/login");
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                         );



                                                  } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }).httpBasic();
        return httpSecurity.build();

    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}
