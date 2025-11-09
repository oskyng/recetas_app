package com.duoc.seguridad_calidad.service;

import com.duoc.seguridad_calidad.model.Recipe;
import com.duoc.seguridad_calidad.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> getAll() {
        return recipeRepository.findAll();
    }

    public List<Recipe> search(String name, String cuisine, String ingredients, String country, String difficulty) {
        return recipeRepository.findAll().stream()
                .filter(r -> name == null || name.isBlank() || r.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(r -> cuisine == null || cuisine.isBlank() || r.getCuisineType().toLowerCase().contains(cuisine.toLowerCase()))
                .filter(r -> ingredients == null || ingredients.isBlank() || r.getIngredients().toLowerCase().contains(ingredients.toLowerCase()))
                .filter(r -> country == null || country.isBlank() || r.getCountryOfOrigin().toLowerCase().contains(country.toLowerCase()))
                .filter(r -> difficulty == null || difficulty.isBlank() || r.getDifficulty().toLowerCase().contains(difficulty.toLowerCase()))
                .toList();
    }
}
