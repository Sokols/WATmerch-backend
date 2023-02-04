package com.qsspy.watmerchbackend.configuration;

import com.qsspy.watmerchbackend.entity.Role;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    public SecurityConfiguration(@Qualifier("authUserDetailsService") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/css/**", "/images/**", "/js/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/hello").permitAll()
                .antMatchers(HttpMethod.POST,"/api/register").permitAll() //register
                .antMatchers(HttpMethod.POST, "/api/loginUser").permitAll() //try to login
                .antMatchers(HttpMethod.POST, "/api/buy").permitAll() //buy items
                .antMatchers(HttpMethod.GET, "/api/categories").permitAll()
                .antMatchers(HttpMethod.GET, "/api/products/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/products").permitAll()
                .antMatchers(HttpMethod.POST, "/api/categories").hasAnyAuthority(Role.RoleType.EMPLOYEE.name())
                .antMatchers(HttpMethod.POST, "/api/products").hasAnyAuthority(Role.RoleType.EMPLOYEE.name())
                .antMatchers(HttpMethod.GET, "/crm/**").hasAuthority(Role.RoleType.ADMIN.name())
                .antMatchers("/chat").permitAll()
                .antMatchers("/chat/support").permitAll()
                .antMatchers("/user/queue/support").permitAll()
                .antMatchers("/topic/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();
    }
}
