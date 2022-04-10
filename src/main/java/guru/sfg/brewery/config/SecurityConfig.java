package guru.sfg.brewery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {



    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
                 http
                 .authorizeRequests(authorize -> {
                    authorize.antMatchers("/", "/webjars/**", "/login", "/resources/**", "/beers/find",
                    "/beers*").permitAll()
                            .antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll()
                            .antMatchers("/h2-console/**").permitAll()
                            .mvcMatchers(HttpMethod.GET,"/api/v1/beerUpc/{upc}").permitAll();


                 })

                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().and()
                .httpBasic();
                 //using these two to enable use of h2-console
                 http.csrf().disable();
                 http.headers().frameOptions().disable();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("spring")
                .password("spring")
                .roles("ADMIN")
                .and()
                .withUser("user")
                .password("password")
                .roles("USER")
                .and()
                .withUser("scott")
                .password("tiger")
                .roles("CUSTOMER");
    }
}
