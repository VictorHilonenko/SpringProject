package spring.scheduler.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import spring.scheduler.entity.enums.Role;
import spring.scheduler.service.UserService;
import spring.scheduler.util.QuickLinkAuthFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;

    @Autowired
    private QuickLinkAuthFilter quickLinkAuthFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilterBefore(quickLinkAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/registration**",
                        "/restore**",
                        "/feedbacks/*/*",
                        "/js/**",
                        "/css/**",
                        "/images/**").permitAll()
                .antMatchers(HttpMethod.GET, "/feedbacks").authenticated()
                .antMatchers(HttpMethod.POST, "/feedbacks").authenticated()
                .antMatchers("/admin**").hasAuthority(Role.ROLE_ADMIN.getAuthority())

                .antMatchers(HttpMethod.GET, "/api/users").hasAuthority(Role.ROLE_ADMIN.getAuthority())
                .antMatchers(HttpMethod.GET, "/api/users/**").authenticated()
                .antMatchers(HttpMethod.POST, "/api/users").denyAll()
                .antMatchers(HttpMethod.PUT, "/api/users").hasAuthority(Role.ROLE_ADMIN.getAuthority())
                .antMatchers(HttpMethod.DELETE, "/api/users/**").hasAuthority(Role.ROLE_ADMIN.getAuthority())

                .antMatchers(HttpMethod.GET, "/api/appointments/*/*").permitAll()
                .antMatchers(HttpMethod.POST, "/api/appointments").hasAuthority(Role.ROLE_USER.getAuthority())
                .antMatchers(HttpMethod.PUT, "/api/appointments/**").hasAuthority(Role.ROLE_MASTER.getAuthority())

                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .permitAll();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }
}