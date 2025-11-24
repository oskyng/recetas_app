package com.duoc.seguridad_calidad.repository;

import com.duoc.seguridad_calidad.model.RecipeMedia;
import com.duoc.seguridad_calidad.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeMediaRepository extends JpaRepository<RecipeMedia, Long> {
    List<RecipeMedia> findByRecipe(Recipe recipe);
}
