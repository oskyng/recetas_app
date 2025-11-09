package com.duoc.seguridad_calidad.controllers;

import com.duoc.seguridad_calidad.model.Recipe;
import com.duoc.seguridad_calidad.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@RequestMapping("/recipes")
public class RecipeController {
    @Autowired
    private RecipeRepository recipeRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("recipes", recipeRepository.findAll());
        return "recipes";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Optional<Recipe> r = recipeRepository.findById(id);
        if (r.isPresent()) {
            model.addAttribute("recipe", r.get());
            return "recipe-detail";
        } else {
            return "redirect:/recipes";
        }
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<?> apiGet(@PathVariable Long id) {
        return recipeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
