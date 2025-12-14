package com.duoc.seguridad_calidad.repository;

import com.duoc.seguridad_calidad.model.Recipe;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RecipeRepositoryTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    void saveAndFindAll_returnsPersistedEntities() {
        Recipe r1 = Recipe.builder().name("Pasta").cuisineType("Italiana").difficulty("Fácil")
                .countryOfOrigin("IT").ingredients("harina, huevo").instructions("mezclar")
                .build();
        Recipe r2 = Recipe.builder().name("Taco").cuisineType("Mexicana").difficulty("Media")
                .countryOfOrigin("MX").ingredients("maíz").instructions("armar")
                .build();

        recipeRepository.save(r1);
        recipeRepository.save(r2);

        List<Recipe> all = recipeRepository.findAll();
        assertThat(all).hasSize(2);
        assertThat(all).anyMatch(r -> r.getName().equals("Pasta"));
        assertThat(all).anyMatch(r -> r.getName().equals("Taco"));
    }
}
