package com.duoc.seguridad_calidad.security;

import com.duoc.seguridad_calidad.config.JwtAuthFilter;
import com.duoc.seguridad_calidad.service.JwtService;
import com.duoc.seguridad_calidad.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JwtAuthFilterTest {

    private MockMvc mockMvc;
    private JwtService jwtService;
    private UserDetailsServiceImpl userDetailsService;

    @RestController
    static class TestController {
        @GetMapping("/protected")
        public String protectedEndpoint() {
            return "OK";
        }
    }

    @BeforeEach
    void setup() {
        jwtService = Mockito.mock(JwtService.class);
        userDetailsService = Mockito.mock(UserDetailsServiceImpl.class);
        JwtAuthFilter filter = new JwtAuthFilter(jwtService, userDetailsService);

        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .addFilters(filter)
                .build();
    }

    @Test
    void noAuthorizationHeader_passesThrough() throws Exception {
        mockMvc.perform(get("/protected").accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    void invalidToken_prefixPresentButParsingFails_passesThrough() throws Exception {
        when(jwtService.extractUsername("bad.token"))
                .thenThrow(new RuntimeException("invalid"));

        mockMvc.perform(get("/protected")
                        .header("Authorization", "Bearer bad.token"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    void validToken_setsAuthentication() throws Exception {
        String token = "good.token";
        when(jwtService.extractUsername(token)).thenReturn("john");
        UserDetails ud = new User("john", "pass",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        when(userDetailsService.loadUserByUsername("john")).thenReturn(ud);
        when(jwtService.isTokenValid(eq(token), any(UserDetails.class))).thenReturn(true);

        mockMvc.perform(get("/protected")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }
}
