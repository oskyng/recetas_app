package com.duoc.seguridad_calidad.service;

import com.duoc.seguridad_calidad.model.Recipe;
import com.duoc.seguridad_calidad.model.RecipeComment;
import com.duoc.seguridad_calidad.repository.RecipeCommentRepository;
import com.duoc.seguridad_calidad.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeCommentServiceTest {

    private RecipeRepository recipeRepository;
    private RecipeCommentRepository commentRepository;
    private RecipeCommentService service;

    private Recipe recipe;

    @BeforeEach
    void setUp() {
        recipeRepository = mock(RecipeRepository.class);
        commentRepository = mock(RecipeCommentRepository.class);
        service = new RecipeCommentService(recipeRepository, commentRepository);
        recipe = Recipe.builder().id(10L).name("Cazuela").build();
    }

    @Test
    void findRecipe_delegatesToRepository() {
        when(recipeRepository.findById(10L)).thenReturn(Optional.of(recipe));
        Optional<Recipe> result = service.findRecipe(10L);
        assertTrue(result.isPresent());
        assertEquals(10L, result.get().getId());
        verify(recipeRepository).findById(10L);
    }

    @Test
    void addComment_buildsEntityAndSaves() {
        when(commentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        RecipeComment saved = service.addComment(recipe, "Muy rica", 5, "ana");

        assertEquals("Muy rica", saved.getText());
        assertEquals(5, saved.getRating());
        assertEquals("ana", saved.getUsername());
        assertEquals(recipe, saved.getRecipe());
        assertNotNull(saved.getCreatedAt());

        assertTrue(saved.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(saved.getCreatedAt().isAfter(LocalDateTime.now().minusSeconds(5)));
    }

    @Test
    void listByRecipe_returnsRepositoryResult() {
        List<RecipeComment> comments = List.of(
                RecipeComment.builder().id(1L).recipe(recipe).text("A").rating(3).username("x").createdAt(LocalDateTime.now()).build()
        );
        when(commentRepository.findByRecipeOrderByCreatedAtDesc(recipe)).thenReturn(comments);

        List<RecipeComment> result = service.listByRecipe(recipe);
        assertEquals(1, result.size());
        verify(commentRepository).findByRecipeOrderByCreatedAtDesc(recipe);
    }

    @Test
    void getAverageRating_empty_returnsZero() {
        when(commentRepository.findByRecipeOrderByCreatedAtDesc(recipe)).thenReturn(List.of());
        assertEquals(0.0, service.getAverageRating(recipe));
    }

    @Test
    void getAverageRating_withComments_returnsAverage() {
        List<RecipeComment> comments = List.of(
                RecipeComment.builder().rating(4).recipe(recipe).text("b").username("u").createdAt(LocalDateTime.now()).build(),
                RecipeComment.builder().rating(2).recipe(recipe).text("c").username("v").createdAt(LocalDateTime.now()).build(),
                RecipeComment.builder().rating(5).recipe(recipe).text("d").username("w").createdAt(LocalDateTime.now()).build()
        );
        when(commentRepository.findByRecipeOrderByCreatedAtDesc(recipe)).thenReturn(comments);

        double avg = service.getAverageRating(recipe);
        assertEquals((4 + 2 + 5) / 3.0, avg, 0.0001);
    }
}
