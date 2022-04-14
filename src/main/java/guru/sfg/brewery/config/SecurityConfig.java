package guru.sfg.brewery.config;

import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //creates filter
    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
        //filter should be applied on all /api urls
        RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    //creates parameter filter
    public RestParameterAuthFilter restParameterAuthFilter(AuthenticationManager authenticationManager) {
        //filter should be applied on all /api urls
        RestParameterAuthFilter filter = new RestParameterAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();

    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //add filter to execute before UsernamePasswordAuthenticationFilter
        //http.addFilterBefore(restHeaderAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
        //        .csrf().disable();

        http.addFilterBefore(restParameterAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();

        http.headers().frameOptions().disable();
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

    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("spring")
                .password("{bcrypt}$2a$10$zajOu/xqdqEwBUl1izpVjO5xBK182doCk/26R5T92x5JHjFjSx8Ka")
                .roles("ADMIN")
                .and()
                .withUser("user")
                .password("{sha256}182f85971dd456128deb99cc6d9bc3dbdd573f512ceaee4865fc06c5954dbd992f6fbc4f060924eb")
                .roles("USER")
                .and()
                .withUser("scott")
                .password("{bcrypt10}$2a$10$O6O2WIRrzGyTr1JyXoNhHeNUPwQasMC1TMKPcxp3F3lr1q5iTpaBW")
                .roles("CUSTOMER");
    }


}
