package com.duoc.seguridad_calidad.service;

import com.duoc.seguridad_calidad.model.Recipe;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShareServiceTest {

    private final ShareService shareService = new ShareService();

    @Test
    void buildSiteUrl_ok() {
        Recipe r = Recipe.builder().id(42L).name("Pan amasado").build();
        assertEquals("/recipes/42", shareService.buildSiteUrl(r));
    }

    @Test
    void buildSocialMessage_ok() {
        Recipe r = Recipe.builder().id(1L).name("Empanadas").build();
        String msg = shareService.buildSocialMessage(r, "cata");
        assertEquals("El usuario cata compartió la receta 'Empanadas'.", msg);
    }
}
