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

        Authority admin = authorityRepository.findAll().stream().filter(e->e.getRole().equalsIgnoreCase("admin")).findFirst().orElse(null);
        if (admin==null) {
            admin = Authority.builder().role("ADMIN").build();
            authorityRepository.save(admin);
        }
        Authority user = authorityRepository.findAll().stream().filter(e->e.getRole().equalsIgnoreCase("user")).findFirst().orElse(null);
        if (user==null) {
            user = Authority.builder().role("USER").build();
            authorityRepository.save(user);
        }
        Authority customer = authorityRepository.findAll().stream().filter(e->e.getRole().equalsIgnoreCase("customer")).findFirst().orElse(null);
        if (customer==null) {
            customer = Authority.builder().role("CUSTOMER").build();
            authorityRepository.save(customer);
        }

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
            user1.getAuthorities().add(user);
            user1.setUsername("user");
            user1.setPassword(passwordEncoder.encode("password"));
            userRepository.save(user1);
            log.debug("User user loaded");
        }

        if (userRepository.findByUsername("scott").isEmpty()) {
            User scott = new User();
            scott.getAuthorities().add(customer);
            scott.setUsername("scott");
            scott.setPassword(passwordEncoder.encode("tiger"));
            userRepository.save(scott);
            log.debug("User scott loaded");
        }

    }
}
