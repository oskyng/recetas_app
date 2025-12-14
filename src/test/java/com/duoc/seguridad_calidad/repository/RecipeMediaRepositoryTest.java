package com.duoc.seguridad_calidad.repository;

import com.duoc.seguridad_calidad.model.Recipe;
import com.duoc.seguridad_calidad.model.RecipeMedia;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RecipeMediaRepositoryTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeMediaRepository mediaRepository;

    @Test
    void findByRecipe_returnsOnlyAssociatedMedia() {
        Recipe recipe1 = recipeRepository.save(Recipe.builder()
                .name("Sushi").cuisineType("Japonesa").difficulty("Alta")
                .countryOfOrigin("JP").ingredients("arroz").instructions("armar")
                .build());
        Recipe recipe2 = recipeRepository.save(Recipe.builder()
                .name("Empanada").cuisineType("Chilena").difficulty("Media")
                .countryOfOrigin("CL").ingredients("harina").instructions("hornear")
                .build());

        mediaRepository.save(RecipeMedia.builder().url("u1").type("IMG").username("a").recipe(recipe1).build());
        mediaRepository.save(RecipeMedia.builder().url("u2").type("VID").username("b").recipe(recipe1).build());
        mediaRepository.save(RecipeMedia.builder().url("u3").type("IMG").username("c").recipe(recipe2).build());

        List<RecipeMedia> list1 = mediaRepository.findByRecipe(recipe1);
        List<RecipeMedia> list2 = mediaRepository.findByRecipe(recipe2);

        assertThat(list1).hasSize(2);
        assertThat(list1).allMatch(m -> m.getRecipe().getId().equals(recipe1.getId()));
        assertThat(list2).hasSize(1);
        assertThat(list2.get(0).getRecipe().getId()).isEqualTo(recipe2.getId());
    }
}
