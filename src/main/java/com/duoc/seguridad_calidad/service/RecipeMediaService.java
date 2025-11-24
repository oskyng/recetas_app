package com.duoc.seguridad_calidad.service;

import com.duoc.seguridad_calidad.model.Recipe;
import com.duoc.seguridad_calidad.model.RecipeMedia;
import com.duoc.seguridad_calidad.repository.RecipeMediaRepository;
import com.duoc.seguridad_calidad.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeMediaService {
    private final RecipeRepository recipeRepository;
    private final RecipeMediaRepository mediaRepository;

    public RecipeMediaService(RecipeRepository recipeRepository,
                              RecipeMediaRepository mediaRepository) {
        this.recipeRepository = recipeRepository;
        this.mediaRepository = mediaRepository;
    }

    public Optional<Recipe> findRecipe(Long recipeId) {
        return recipeRepository.findById(recipeId);
    }

    public RecipeMedia addMedia(Recipe recipe, String url, String type, String username) {
        RecipeMedia media = RecipeMedia.builder()
                .url(url)
                .type(type)
                .username(username)
                .recipe(recipe)
                .build();
        return mediaRepository.save(media);
    }

    public List<RecipeMedia> listByRecipe(Recipe recipe) {
        return mediaRepository.findByRecipe(recipe);
    }
}
