package com.duoc.seguridad_calidad.config;

import com.duoc.seguridad_calidad.repository.RecipeRepository;
import com.duoc.seguridad_calidad.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DataInitializerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    void commandLineRunner_populatesUsersAndRecipes_whenEmpty() throws Exception {
        userRepository.deleteAll();
        recipeRepository.deleteAll();
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        DataInitializer initializer = new DataInitializer();
        initializer.run(userRepository, recipeRepository, encoder).run(new String[]{});

        assertThat(userRepository.count()).isGreaterThan(0);
        assertThat(recipeRepository.count()).isGreaterThan(0);
    }
}
