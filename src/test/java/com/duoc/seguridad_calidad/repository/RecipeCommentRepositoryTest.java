package com.duoc.seguridad_calidad.repository;

import com.duoc.seguridad_calidad.model.Recipe;
import com.duoc.seguridad_calidad.model.RecipeComment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RecipeCommentRepositoryTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeCommentRepository commentRepository;

    @Test
    void findByRecipeOrderByCreatedAtDesc_returnsInDescendingOrder() {
        Recipe recipe = recipeRepository.save(Recipe.builder()
                .name("Arepa").cuisineType("Venezolana").difficulty("Fácil")
                .countryOfOrigin("VE").ingredients("maíz").instructions("cocinar")
                .build());

        RecipeComment c1 = RecipeComment.builder()
                .text("Buena").rating(5).username("ana")
                .createdAt(LocalDateTime.now().minusHours(2))
                .recipe(recipe).build();
        RecipeComment c2 = RecipeComment.builder()
                .text("Ok").rating(3).username("luis")
                .createdAt(LocalDateTime.now())
                .recipe(recipe).build();
        commentRepository.saveAll(List.of(c1, c2));

        List<RecipeComment> result = commentRepository.findByRecipeOrderByCreatedAtDesc(recipe);
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCreatedAt()).isAfterOrEqualTo(result.get(1).getCreatedAt());
        assertThat(result.get(0).getText()).isEqualTo("Ok");
        assertThat(result.get(1).getText()).isEqualTo("Buena");
    }
}
