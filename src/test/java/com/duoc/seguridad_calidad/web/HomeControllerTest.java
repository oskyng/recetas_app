package com.duoc.seguridad_calidad.web;

import com.duoc.seguridad_calidad.controllers.HomeController;
import com.duoc.seguridad_calidad.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HomeControllerTest {

    private MockMvc mockMvc;
    private RecipeRepository recipeRepository;

    @BeforeEach
    void setup() {
        recipeRepository = Mockito.mock(RecipeRepository.class);
        HomeController controller = new HomeController(recipeRepository);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void indexRoutes_renderIndexWithRecipesAttribute() throws Exception {
        when(recipeRepository.findAll()).thenReturn(List.of());

        for (String path : List.of("/", "/index", "/home")) {
            mockMvc.perform(get(path))
                    .andExpect(status().isOk())
                    .andExpect(view().name("index"))
                    .andExpect(model().attributeExists("recipes"));
        }
    }

    @Test
    void loginRoute_rendersLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }
}
