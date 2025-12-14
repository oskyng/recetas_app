package com.duoc.seguridad_calidad.service;

import com.duoc.seguridad_calidad.model.Recipe;
import com.duoc.seguridad_calidad.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class RecipeServiceTest {

    private RecipeRepository recipeRepository;
    private RecipeService recipeService;

    @BeforeEach
    void setUp() {
        recipeRepository = Mockito.mock(RecipeRepository.class);
        recipeService = new RecipeService(recipeRepository);

        List<Recipe> data = List.of(
                Recipe.builder().id(1L).name("Paella Valenciana").cuisineType("Mediterránea")
                        .difficulty("Media").countryOfOrigin("España").ingredients("Arroz, Pollo, Mariscos")
                        .instructions("Cocinar")
                        .build(),
                Recipe.builder().id(2L).name("Sushi").cuisineType("Japonesa")
                        .difficulty("Alta").countryOfOrigin("Japón").ingredients("Arroz, Pescado")
                        .instructions("Enrollar")
                        .build(),
                Recipe.builder().id(3L).name("Tacos al pastor").cuisineType("Mexicana")
                        .difficulty("Baja").countryOfOrigin("México").ingredients("Tortilla, Cerdo, Piña")
                        .instructions("Armar")
                        .build()
        );
        when(recipeRepository.findAll()).thenReturn(data);
    }

    @Test
    void searchByName_isCaseInsensitive_andMatchesPartials() {
        List<Recipe> result = recipeService.search("sush", null, null, null, null);
        assertEquals(1, result.size());
        assertEquals("Sushi", result.get(0).getName());
    }

    @Test
    void searchByMultipleFilters_returnsExpected() {
        List<Recipe> result = recipeService.search("paella", "medit", "pollo", "espa", "med");
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void nullOrBlankFilters_doNotRestrict() {
        assertEquals(3, recipeService.search(null, null, null, null, null).size());
        assertEquals(3, recipeService.search("", " ", "", " ", "").size());
    }

    @Test
    void noMatches_returnsEmptyList() {
        List<Recipe> result = recipeService.search("no-existe", null, null, null, null);
        assertEquals(0, result.size());
    }
}
