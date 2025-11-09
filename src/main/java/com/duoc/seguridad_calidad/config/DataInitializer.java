package com.duoc.seguridad_calidad.config;

import com.duoc.seguridad_calidad.model.Recipe;
import com.duoc.seguridad_calidad.model.User;
import com.duoc.seguridad_calidad.repository.RecipeRepository;
import com.duoc.seguridad_calidad.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner run(UserRepository userRepository, RecipeRepository recipeRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                userRepository.save(User.builder()
                        .fullName("Oscar Sanzana")
                        .username("admin")
                        .password(passwordEncoder.encode("Password123"))
                        .role("ADMIN")
                        .build());
                userRepository.save(User.builder()
                        .fullName("Bob Farmer")
                        .username("bob")
                        .password(passwordEncoder.encode("Password123"))
                        .role("USER")
                        .build());
                userRepository.save(User.builder()
                        .fullName("Charlie Cook")
                        .username("charlie")
                        .password(passwordEncoder.encode("Password123"))
                        .role("USER")
                        .build());
            }

            if (recipeRepository.count() == 0) {
                recipeRepository.save(Recipe.builder()
                        .name("Empanadas de Pino")
                        .cuisineType("Chilean")
                        .difficulty("Medium")
                        .countryOfOrigin("Chile")
                        .ingredients("Beef, onion, paprika, olives, eggs, dough")
                        .instructions("Mix, fill, bake.")
                        .build());
                recipeRepository.save(Recipe.builder()
                        .name("Tomato Pasta")
                        .cuisineType("Italian")
                        .difficulty("Easy")
                        .countryOfOrigin("Italy")
                        .ingredients("Pasta, tomatoes, garlic, olive oil")
                        .instructions("Cook pasta, prepare sauce, combine.")
                        .build());
            }
        };
    }
}
