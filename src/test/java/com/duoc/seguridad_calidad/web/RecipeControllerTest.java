package com.duoc.seguridad_calidad.web;

import com.duoc.seguridad_calidad.controllers.RecipeController;
import com.duoc.seguridad_calidad.model.Recipe;
import com.duoc.seguridad_calidad.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RecipeControllerTest {

    private MockMvc mockMvc;
    private RecipeRepository recipeRepository;

    @BeforeEach
    void setup() {
        recipeRepository = Mockito.mock(RecipeRepository.class);
        RecipeController controller = new RecipeController();
        try {
            var field = RecipeController.class.getDeclaredField("recipeRepository");
            field.setAccessible(true);
            field.set(controller, recipeRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void list_recipesViewWithModelAttribute() throws Exception {
        when(recipeRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/recipes"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipes"))
                .andExpect(model().attributeExists("recipes"));
    }

    @Test
    void detail_existingRecipe_rendersDetailView() throws Exception {
        Recipe r = Recipe.builder().id(10L).name("Paella").build();
        when(recipeRepository.findById(10L)).thenReturn(Optional.of(r));

        mockMvc.perform(get("/recipes/10"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe-detail"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    void detail_missingRecipe_redirectsToList() throws Exception {
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/recipes/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recipes"));
    }

    @Test
    void json_getAll_returnsOkWithJson() throws Exception {
        when(recipeRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/recipes/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void apiGet_existingRecipe_returnsOk() throws Exception {
        Recipe r = Recipe.builder().id(5L).name("Sopa").build();
        when(recipeRepository.findById(5L)).thenReturn(Optional.of(r));

        mockMvc.perform(get("/recipes/api/5"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void apiGet_missingRecipe_returns404() throws Exception {
        when(recipeRepository.findById(123L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/recipes/api/123"))
                .andExpect(status().isNotFound());
    }
}
