package account.security;

import account.model.user.Role;
import account.security.login.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfiguration {
    private final AuthenticationEntryPoint restAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .csrf().disable().headers().frameOptions().disable()
                .and()
                .authorizeRequests(auth -> {
                    auth.antMatchers("/api/auth/signup", "/h2-console/**", "/login").permitAll();
                    auth.antMatchers("/api/auth/changepass").hasAnyAuthority(Role.ROLE_USER.name(), Role.ROLE_ACCOUNTANT.name(), Role.ROLE_ADMINISTRATOR.name());
                    auth.antMatchers("/api/acct/payments").hasAuthority(Role.ROLE_ACCOUNTANT.name());
                    auth.antMatchers("/api/empl/payment").hasAnyAuthority(Role.ROLE_USER.name(), Role.ROLE_ACCOUNTANT.name());
                    auth.antMatchers("/api/admin/**").hasAnyAuthority(Role.ROLE_ADMINISTRATOR.name());
                    auth.antMatchers("/api/security/events").hasAuthority(Role.ROLE_AUDITOR.name());
                })
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);
        return http.build();
    }

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(13);
    }

}
