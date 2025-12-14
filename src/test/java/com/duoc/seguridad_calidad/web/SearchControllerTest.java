package com.duoc.seguridad_calidad.web;

import com.duoc.seguridad_calidad.controllers.SearchController;
import com.duoc.seguridad_calidad.model.Recipe;
import com.duoc.seguridad_calidad.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SearchControllerTest {

    private MockMvc mockMvc;
    private RecipeService recipeService;

    @BeforeEach
    void setup() {
        recipeService = Mockito.mock(RecipeService.class);
        SearchController controller = new SearchController(recipeService);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void getSearchForm_returnsSearchView() throws Exception {
        mockMvc.perform(get("/recipes/search"))
                .andExpect(status().isOk())
                .andExpect(view().name("search"));
    }

    @Test
    void getResults_returnsSearchResultsViewWithRecipes() throws Exception {
        Recipe r = Recipe.builder()
                .id(1L)
                .name("Tortilla")
                .cuisineType("Española")
                .difficulty("Fácil")
                .countryOfOrigin("ES")
                .ingredients("huevo, papa")
                .instructions("...")
                .build();
        when(recipeService.search(any(), any(), any(), any(), any()))
                .thenReturn(List.of(r));

        mockMvc.perform(get("/recipes/results")
                        .param("name", "Tor")
                        .param("cuisine", "Esp")
                        .param("ingredients", "huevo")
                        .param("country", "ES")
                        .param("difficulty", "Fácil"))
                .andExpect(status().isOk())
                .andExpect(view().name("search-results"))
                .andExpect(model().attributeExists("recipes"));
    }
}
