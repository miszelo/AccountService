package account.security;

import account.model.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfiguration {

    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions().disable())
                .authorizeRequests(auth -> {
                    auth.antMatchers("/api/auth/signup", "/h2-console/**").permitAll();
                    auth.antMatchers("/api/auth/changepass").hasAnyAuthority(Role.ROLE_USER.name(), Role.ROLE_ACCOUNTANT.name(), Role.ROLE_ADMINISTRATOR.name());
                    auth.antMatchers("/api/acct/payments").hasAnyAuthority(Role.ROLE_ACCOUNTANT.name());
                    auth.antMatchers("/api/empl/payment").hasAnyAuthority(Role.ROLE_USER.name(), Role.ROLE_ACCOUNTANT.name());
                    auth.antMatchers("/api/admin/**").hasAnyAuthority(Role.ROLE_ADMINISTRATOR.name());
                })
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .userDetailsService(userDetailsService)
                .httpBasic(Customizer.withDefaults())
                .build();
    }


    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(13);
    }

}
