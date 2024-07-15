package uk.ac.cardiff.phoenix.ilm.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import uk.ac.cardiff.phoenix.ilm.users.serviceImpl.RolePermissionServiceImpl;


@Configuration
public class SecurityConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(RolePermissionServiceImpl.class);


    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        AccessDeniedHandlerImpl handler = new AccessDeniedHandlerImpl();
        handler.setErrorPage("/notauthorised"); // Specify the URL of your custom page
        logger.info("Access DENIED handler invoked");
        return handler;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordUtil();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .accessDeniedHandler(accessDeniedHandler())
                )
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers ("/login").permitAll()
                                .requestMatchers ("/CSS/**").permitAll()
                                .requestMatchers("/notauthorised").permitAll()
                                .requestMatchers("/error").permitAll()
                                .requestMatchers ("/").permitAll()
                                .requestMatchers ("/levels/**").permitAll()
                                .requestMatchers ("/workshops/**").permitAll()
                                .requestMatchers ("/user/**").hasAnyRole("USER","SU")
                                .requestMatchers ("/manager/**").hasAnyRole("MANAGER","SU")
                                .requestMatchers ("/admin/**").hasAnyRole("ADMIN","SU")
                                .requestMatchers ("/submission/**","/lookup/**").hasAnyRole("SU","ADMIN")
                                .requestMatchers ("/candidate/**").hasAnyRole("SU","ADMIN","MANAGER")
                                .anyRequest().denyAll()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login").permitAll()
                                .successHandler((request, response, authentication) -> {
                                    // Log or print statements to check if the handler is invoked
                                    String userName = authentication.getName();
                                    logger.info("User logged in. User: " + userName);
                                    response.sendRedirect("/");
                                })
                                .failureUrl("/login?error")
                                .failureHandler((request, response, exception) -> {
                                    // Log or print statements to check if the handler is invoked
                                    String userName = request.getParameter("username");
                                    logger.info("User login failed. User: {}" + userName);
                                    response.sendRedirect("/login?error");
                                })
                )
                .logout(logout ->
                        logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .logoutSuccessUrl("/") // Redirect to login page after logout
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                                .permitAll()
                                .logoutSuccessHandler((request, response, authentication) -> {
                                    // Log or print statements to check if the handler is invoked
                                    logger.info("User logged out");
                                    response.sendRedirect("/login");
                                })
                );

        return http.build();

    }


}