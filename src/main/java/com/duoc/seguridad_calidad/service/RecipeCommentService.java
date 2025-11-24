package com.duoc.seguridad_calidad.service;

import com.duoc.seguridad_calidad.model.Recipe;
import com.duoc.seguridad_calidad.model.RecipeComment;
import com.duoc.seguridad_calidad.repository.RecipeCommentRepository;
import com.duoc.seguridad_calidad.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeCommentService {

    private final RecipeRepository recipeRepository;
    private final RecipeCommentRepository commentRepository;

    public RecipeCommentService(RecipeRepository recipeRepository,
                                RecipeCommentRepository commentRepository) {
        this.recipeRepository = recipeRepository;
        this.commentRepository = commentRepository;
    }

    public Optional<Recipe> findRecipe(Long recipeId) {
        return recipeRepository.findById(recipeId);
    }

    public RecipeComment addComment(Recipe recipe, String text, int rating, String username) {
        RecipeComment comment = RecipeComment.builder()
                .text(text)
                .rating(rating)
                .username(username)
                .createdAt(LocalDateTime.now())
                .recipe(recipe)
                .build();
        return commentRepository.save(comment);
    }

    public List<RecipeComment> listByRecipe(Recipe recipe) {
        return commentRepository.findByRecipeOrderByCreatedAtDesc(recipe);
    }

    public double getAverageRating(Recipe recipe) {
        List<RecipeComment> comments = listByRecipe(recipe);
        if (comments.isEmpty()) {
            return 0.0;
        }
        DoubleSummaryStatistics stats = comments.stream()
                .mapToDouble(RecipeComment::getRating)
                .summaryStatistics();
        return stats.getAverage();
    }
}