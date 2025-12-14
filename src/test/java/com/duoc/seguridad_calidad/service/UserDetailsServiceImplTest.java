package com.duoc.seguridad_calidad.service;

import com.duoc.seguridad_calidad.model.User;
import com.duoc.seguridad_calidad.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class UserDetailsServiceImplTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void loadUserByUsername_returnsSpringUserWithAuthorities() {
        userRepository.save(User.builder()
                .fullName("Alice")
                .username("alice")
                .password("{noop}pwd")
                .role("USER")
                .build());

        UserDetailsServiceImpl service = new UserDetailsServiceImpl(userRepository);

        UserDetails ud = service.loadUserByUsername("alice");

        assertThat(ud.getUsername()).isEqualTo("alice");
        assertThat(ud.getAuthorities()).extracting("authority").contains("ROLE_USER");
    }

    @Test
    void loadUserByUsername_throwsWhenNotFound() {
        UserDetailsServiceImpl service = new UserDetailsServiceImpl(userRepository);
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("missing"));
    }
}
