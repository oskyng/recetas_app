package com.duoc.seguridad_calidad.service;

import com.duoc.seguridad_calidad.model.Recipe;
import org.springframework.stereotype.Service;

@Service
public class ShareService {

    public String buildSiteUrl(Recipe recipe) {
        return "/recipes/" + recipe.getId();
    }

    public String buildSocialMessage(Recipe recipe, String username) {
        return "El usuario " + username + " compartió la receta '" +
                recipe.getName() + "'.";
    }
}