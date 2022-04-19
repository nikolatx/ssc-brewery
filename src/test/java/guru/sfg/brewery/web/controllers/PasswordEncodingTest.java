package guru.sfg.brewery.web.controllers;

import org.assertj.core.internal.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

import javax.naming.ldap.LdapContext;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
public class PasswordEncodingTest {

    static final String PASSWORD = "password";

    @Test
    void testBcrypt() {
        PasswordEncoder bcrypt = new BCryptPasswordEncoder(10);

        System.out.println(bcrypt.encode(PASSWORD));
        System.out.println(bcrypt.encode(PASSWORD));
        System.out.println(bcrypt.encode("tiger"));
    }

    @Test
    void testSha256() {
        PasswordEncoder sha256 = new StandardPasswordEncoder();
        String pass1 = sha256.encode(PASSWORD);
        String pass2 = sha256.encode(PASSWORD);

        System.out.println(pass1);
        System.out.println(pass2);
        assertTrue(sha256.matches(PASSWORD, pass1));
        assertTrue(sha256.matches(PASSWORD, pass2));
        System.out.println(sha256.encode("password"));

    }

    @Test
    void testLdap() {
        PasswordEncoder ldap = new LdapShaPasswordEncoder();
        System.out.println(ldap.encode(PASSWORD));
        System.out.println(ldap.encode(PASSWORD));
        System.out.println(ldap.encode("tiger"));


        String encodedPassword = ldap.encode(PASSWORD);
        String encodedPassword1 = ldap.encode(PASSWORD);
        assertTrue(ldap.matches(PASSWORD, encodedPassword));
        assertTrue(ldap.matches(PASSWORD, encodedPassword1));
    }


    @Test
    void testNoOp() {
        PasswordEncoder noOp = NoOpPasswordEncoder.getInstance();
        System.out.println(noOp.encode(PASSWORD));
    }

    @Test
    void hashingExample() {
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));

        String salted = PASSWORD + "ThisIsMySaltValue";
        System.out.println(DigestUtils.md5DigestAsHex(salted.getBytes()));
    }

}
