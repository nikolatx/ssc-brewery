package guru.sfg.brewery.config;

import guru.sfg.brewery.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PersistentTokenRepository persistentTokenRepository;

    //needed for use with Spring Data JPA SPeL
    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //h2 console config
        http.headers().frameOptions().sameOrigin();
        //http.csrf().disable();

        http
            .authorizeRequests(authorize -> {
                authorize
                        .antMatchers("/h2-console/**").permitAll()
                        .antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll();
                        //.mvcMatchers("/brewery/breweries*").hasAnyRole("ADMIN", "CUSTOMER")
                       // .mvcMatchers(HttpMethod.GET, "/brewery/api/v1/breweries").hasAnyRole("ADMIN", "CUSTOMER");
                        //.mvcMatchers("/beers/find", "/beers/{beerId}").hasAnyRole("ADMIN", "CUSTOMER", "USER");

             })
            .authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .formLogin(loginConfigurer -> {
                loginConfigurer.loginProcessingUrl("/login")
                        .loginPage("/").permitAll()
                        .successForwardUrl("/")
                        .defaultSuccessUrl("/")
                        .failureUrl("/?error");
            })
                .logout(logoutConfigurer -> {
                    logoutConfigurer.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                            .logoutSuccessUrl("/?logout")
                            .permitAll();
                })
            .httpBasic()
            .and().csrf().ignoringAntMatchers("/h2-console/**", "/api/**")
                .and().rememberMe().tokenRepository(persistentTokenRepository).userDetailsService(userDetailsService);
                //.rememberMe().key("tnt-key").userDetailsService(userDetailsService);
    }



//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //auth.userDetailsService(this.jpaUserDetailsService).passwordEncoder(passwordEncoder());

        /*
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
         */
 //   }


}
