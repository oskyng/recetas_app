package com.duoc.seguridad_calidad.service;

import com.duoc.seguridad_calidad.model.User;
import com.duoc.seguridad_calidad.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User appUser = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + appUser.getRole()));
        return new org.springframework.security.core.userdetails.User(appUser.getUsername(), appUser.getPassword(), authorities);
    }
}
