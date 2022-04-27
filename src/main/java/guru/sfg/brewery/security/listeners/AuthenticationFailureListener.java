package guru.sfg.brewery.security.listeners;

import guru.sfg.brewery.domain.security.LoginFailure;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.LoginFailureRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticationFailureListener {

    private final LoginFailureRepository loginFailureRepository;
    private final UserRepository userRepository;

    @EventListener
    public void listen(AuthenticationFailureBadCredentialsEvent authenticationFailureBadCredentialsEvent) {

        log.error("Authentication failure, bad credentials!");
        if (authenticationFailureBadCredentialsEvent.getSource() instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authenticationFailureBadCredentialsEvent.getSource();
            LoginFailure.LoginFailureBuilder builder = LoginFailure.builder();

            if (token.getPrincipal() instanceof String) {
                log.error("Authentication failure for username: " + token.getPrincipal());
                builder.username((String) token.getPrincipal());
                userRepository.findByUsername((String) token.getPrincipal()).ifPresent(builder::user);
            }

            if (token.getDetails() instanceof WebAuthenticationDetails) {
                WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) token.getDetails();
                builder.IpAddress(webAuthenticationDetails.getRemoteAddress());
                log.error("IP address: " + webAuthenticationDetails.getRemoteAddress());
            }
            LoginFailure loginFailure = loginFailureRepository.save(builder.build());
            log.debug("Login Failure for username: "+ loginFailure.getUsername());
            
            if(loginFailure.getUser() != null) {
                lockUserAccount(loginFailure.getUser());
            }
            
        }
    }

    private void lockUserAccount(User user) {

        List<LoginFailure> failures = loginFailureRepository.findAllByUserAndCreatedDateIsAfter(user, Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
        if (failures.size() > 3) {
            log.debug("Locking user account");
            user.setAccountNonLocked(false);
            userRepository.save(user);
        }

    }

}
