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
        httpSecurity.authorizeHttpRequests((auth) -> {
            try {
                auth.antMatchers("/user/registration","/user/login","/","/allDestinations","/rooms","/rooms/detailsForUser","/regionManager/details","/destinations","/destinations/details").permitAll()
                        .antMatchers("/user/usersManager","/user/delete/{id}","/user/changeRole","/rooms/add","/rooms/delete","/rooms/update","/rooms/details","/regionManager","/regionManager/update","/regionManager/delete","/hotels/create","/hotels/hotelManager", "/hotels/delete","/hotels/update","/hotels/details","/hotels/addBoardBasis", "/hotels/deleteBoardBasis","/destinations/destinationManager","/destinations/delete","/destinations/destinationManager/update","/cart/cancelBooking","/cart/allBookings","/adminPage").hasRole("ADMIN")
                        .antMatchers( "/user/update","/user/delete","/user/accountManager","/rooms/book","/rooms/prebook/","/review/*","/hotels/detailsForUser","/cart","/cart/book","/cart/deleteFromCart").authenticated()
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
                        .addLogoutHandler((request, response, authentication) -> {

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
    PasswordEncoder passwordEncoder() {

        return NoOpPasswordEncoder.getInstance();
    }
}
