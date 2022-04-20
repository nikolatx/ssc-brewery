package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.Role;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.RoleRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserLoader implements CommandLineRunner {

    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        loadUserData();

    }

    private void loadUserData() {

        //beer auths
        Authority createBeer = authorityRepository.save(Authority.builder().permission("beer.create").build());
        Authority updateBeer = authorityRepository.save(Authority.builder().permission("beer.update").build());
        Authority readBeer = authorityRepository.save(Authority.builder().permission("beer.read").build());
        Authority deleteBeer = authorityRepository.save(Authority.builder().permission("beer.delete").build());

        Role adminRole = roleRepository.save(Role.builder().name("ADMIN").build());
        Role customerRole = roleRepository.save(Role.builder().name("CUSTOMER").build());
        Role userRole = roleRepository.save(Role.builder().name("USER").build());

        adminRole.setAuthorities(Set.of(createBeer, updateBeer, readBeer, deleteBeer));
        customerRole.setAuthorities(Set.of(readBeer));
        userRole.setAuthorities(Set.of(readBeer));

        roleRepository.saveAll(Arrays.asList(adminRole, customerRole, userRole));

        User user = User.builder()
                .username("spring")
                .password(passwordEncoder.encode("guru"))
                .role(adminRole)
                .build();

        userRepository.save(user);

        userRepository.save(User.builder().username("user")
                .password(passwordEncoder.encode("password"))
                .role(userRole)
                .build());

        userRepository.save(User.builder().username("scott")
                .password(passwordEncoder.encode("tiger"))
                .role(customerRole)
                .build());

        /*
        if (userRepository.findByUsername("spring").isEmpty()) {
            User spring = new User();
            spring.getRoles().add(adminRole);
            spring.setUsername("spring");
            spring.setPassword(passwordEncoder.encode("guru"));
            userRepository.save(spring);
            log.debug("User spring loaded");
        }

        if (userRepository.findByUsername("user").isEmpty()) {
            User user1 = new User();
            user1.getRoles().add(userRole);
            user1.setUsername("user");
            user1.setPassword(passwordEncoder.encode("password"));
            userRepository.save(user1);
            log.debug("User user loaded");
        }

        if (userRepository.findByUsername("scott").isEmpty()) {
            User scott = new User();
            scott.getRoles().add(customerRole);
            scott.setUsername("scott");
            scott.setPassword(passwordEncoder.encode("tiger"));
            userRepository.save(scott);
            log.debug("User scott loaded");
        }

         */

    }
}
