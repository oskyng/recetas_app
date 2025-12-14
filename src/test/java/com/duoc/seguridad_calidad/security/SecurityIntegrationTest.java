package com.duoc.seguridad_calidad.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void publicRoutes_return200() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/index"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/recipes"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/recipes/search"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/recipes/results"))
                .andExpect(status().isOk());
    }

    @Test
    void protectedRoute_withoutAuth_returns403() throws Exception {
        mockMvc.perform(get("/recipes/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "bob", roles = {"USER"})
    void adminRoute_withUserRole_returns403() throws Exception {
        mockMvc.perform(get("/admin/panel"))
                .andExpect(status().isForbidden());
    }
}
