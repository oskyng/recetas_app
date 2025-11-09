package com.duoc.seguridad_calidad.controllers;

import com.duoc.seguridad_calidad.model.Recipe;
import com.duoc.seguridad_calidad.service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/recipes")
public class SearchController {
    private final RecipeService recipeService;

    public SearchController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/search")
    public String searchForm() {
        return "search";
    }

    @GetMapping("/results")
    public String searchResults(@RequestParam(required = false) String name,
                                @RequestParam(required = false) String cuisine,
                                @RequestParam(required = false) String ingredients,
                                @RequestParam(required = false) String country,
                                @RequestParam(required = false) String difficulty,
                                Model model) {

        List<Recipe> results = recipeService.search(name, cuisine, ingredients, country, difficulty);
        model.addAttribute("recipes", results);
        return "search-results";
    }
}
