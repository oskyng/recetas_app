package com.duoc.seguridad_calidad.controllers;

import com.duoc.seguridad_calidad.repository.RecipeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class HomeController {

    private final RecipeRepository recipeRepository;

    public HomeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @GetMapping({"/", "/index", "/home"})
    public String index(Model model) {
        model.addAttribute("recipes", recipeRepository.findAll());
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
