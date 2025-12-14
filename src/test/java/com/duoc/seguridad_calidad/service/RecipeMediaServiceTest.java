package com.duoc.seguridad_calidad.service;

import com.duoc.seguridad_calidad.model.Recipe;
import com.duoc.seguridad_calidad.model.RecipeMedia;
import com.duoc.seguridad_calidad.repository.RecipeMediaRepository;
import com.duoc.seguridad_calidad.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeMediaServiceTest {

    private RecipeRepository recipeRepository;
    private RecipeMediaRepository mediaRepository;
    private RecipeMediaService service;

    private Recipe recipe;

    @BeforeEach
    void setUp() {
        recipeRepository = mock(RecipeRepository.class);
        mediaRepository = mock(RecipeMediaRepository.class);
        service = new RecipeMediaService(recipeRepository, mediaRepository);
        recipe = Recipe.builder().id(7L).name("Queque").build();
    }

    @Test
    void findRecipe_delegatesToRepository() {
        when(recipeRepository.findById(7L)).thenReturn(Optional.of(recipe));
        assertTrue(service.findRecipe(7L).isPresent());
        verify(recipeRepository).findById(7L);
    }

    @Test
    void addMedia_buildsAndSaves() {
        when(mediaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        RecipeMedia media = service.addMedia(recipe, "http://img/1.jpg", "image", "maria");
        assertEquals("http://img/1.jpg", media.getUrl());
        assertEquals("image", media.getType());
        assertEquals("maria", media.getUsername());
        assertEquals(recipe, media.getRecipe());
    }

    @Test
    void listByRecipe_returnsRepositoryResult() {
        List<RecipeMedia> list = List.of(
                RecipeMedia.builder().id(1L).url("u").type("image").username("x").recipe(recipe).build()
        );
        when(mediaRepository.findByRecipe(recipe)).thenReturn(list);

        List<RecipeMedia> result = service.listByRecipe(recipe);
        assertEquals(1, result.size());
        verify(mediaRepository).findByRecipe(recipe);
    }
}
