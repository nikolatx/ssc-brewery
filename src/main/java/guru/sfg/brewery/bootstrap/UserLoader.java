package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserLoader implements CommandLineRunner {

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        loadUserData();

    }

    private void loadUserData() {
        Authority admin = Authority.builder().role("ADMIN").build();
        Authority user = Authority.builder().role("USER").build();
        Authority customer = Authority.builder().role("CUSTOMER").build();

        if (authorityRepository.findAll().stream().noneMatch(e->e.getRole().equalsIgnoreCase(admin.getRole())))
            authorityRepository.save(admin);
        if (authorityRepository.findAll().stream().noneMatch(e->e.getRole().equalsIgnoreCase(user.getRole())))
            authorityRepository.save(user);
        if (authorityRepository.findAll().stream().noneMatch(e->e.getRole().equalsIgnoreCase(customer.getRole())))
            authorityRepository.save(customer);

        if (userRepository.findByUsername("spring").isEmpty()) {
            User spring = new User();
            spring.getAuthorities().add(admin);
            spring.setUsername("spring");
            spring.setPassword(passwordEncoder.encode("guru"));
            userRepository.save(spring);
            log.debug("User spring loaded");
        }

        if (userRepository.findByUsername("user").isEmpty()) {
            User user1 = new User();
            user1.getAuthorities().add(admin);
            user1.setUsername("user");
            user1.setPassword(passwordEncoder.encode("password"));
            userRepository.save(user1);
            log.debug("User user loaded");
        }

        if (userRepository.findByUsername("scott").isEmpty()) {
            User scott = new User();
            scott.getAuthorities().add(admin);
            scott.setUsername("scott");
            scott.setPassword(passwordEncoder.encode("tiger"));
            userRepository.save(scott);
            log.debug("User scott loaded");
        }

    }
}
