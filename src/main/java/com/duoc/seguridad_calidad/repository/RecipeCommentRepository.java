package com.duoc.seguridad_calidad.repository;

import com.duoc.seguridad_calidad.model.RecipeComment;
import com.duoc.seguridad_calidad.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeCommentRepository extends JpaRepository<RecipeComment, Long> {
    List<RecipeComment> findByRecipeOrderByCreatedAtDesc(Recipe recipe);
}