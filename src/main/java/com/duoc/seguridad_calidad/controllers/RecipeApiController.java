package com.duoc.seguridad_calidad.controllers;

import com.duoc.seguridad_calidad.dto.AddMediaRequest;
import com.duoc.seguridad_calidad.dto.CommentRequest;
import com.duoc.seguridad_calidad.dto.ShareResponse;
import com.duoc.seguridad_calidad.model.Recipe;
import com.duoc.seguridad_calidad.model.RecipeComment;
import com.duoc.seguridad_calidad.model.RecipeMedia;
import com.duoc.seguridad_calidad.service.RecipeCommentService;
import com.duoc.seguridad_calidad.service.RecipeMediaService;
import com.duoc.seguridad_calidad.service.ShareService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipes")
public class RecipeApiController {

    private final RecipeMediaService mediaService;
    private final RecipeCommentService commentService;
    private final ShareService shareService;

    public RecipeApiController(RecipeMediaService mediaService,
                               RecipeCommentService commentService,
                               ShareService shareService) {
        this.mediaService = mediaService;
        this.commentService = commentService;
        this.shareService = shareService;
    }

    @PostMapping("/{id}/media")
    public ResponseEntity<Object> addMedia(@PathVariable Long id,
                                      @RequestBody AddMediaRequest request,
                                      Authentication authentication) {
        Optional<Recipe> recipeOpt = mediaService.findRecipe(id);
        if (recipeOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Recipe recipe = recipeOpt.get();
        String username = authentication.getName();

        RecipeMedia saved = mediaService.addMedia(
                recipe,
                request.getUrl(),
                request.getType(),
                username
        );

        return ResponseEntity
                .created(URI.create("/api/recipes/" + id + "/media/" + saved.getId()))
                .body(saved);
    }

    @GetMapping("/{id}/media")
    public ResponseEntity<Object> listMedia(@PathVariable Long id) {
        Optional<Recipe> recipeOpt = mediaService.findRecipe(id);
        if (recipeOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<RecipeMedia> list = mediaService.listByRecipe(recipeOpt.get());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/{id}/share")
    public ResponseEntity<Object> share(@PathVariable Long id,
                                   Authentication authentication) {
        Optional<Recipe> recipeOpt = mediaService.findRecipe(id);
        if (recipeOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Recipe recipe = recipeOpt.get();
        String username = authentication.getName();

        String url = shareService.buildSiteUrl(recipe);
        String message = shareService.buildSocialMessage(recipe, username);

        ShareResponse response = new ShareResponse(message, url);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Object> addComment(@PathVariable Long id, @RequestBody CommentRequest request, Authentication authentication) {

        if (request.getRating() < 1 || request.getRating() > 5) {
            return ResponseEntity.badRequest().body("Rating debe estar entre 1 y 5");
        }

        Optional<Recipe> recipeOpt = commentService.findRecipe(id);
        if (recipeOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Recipe recipe = recipeOpt.get();
        String username = authentication.getName();

        RecipeComment saved = commentService.addComment(
                recipe,
                request.getText(),
                request.getRating(),
                username
        );

        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<Object> listComments(@PathVariable Long id) {
        Optional<Recipe> recipeOpt = commentService.findRecipe(id);
        if (recipeOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Recipe recipe = recipeOpt.get();
        List<RecipeComment> comments = commentService.listByRecipe(recipe);
        double avg = commentService.getAverageRating(recipe);

        return ResponseEntity.ok(new Object() {
            public final List<RecipeComment> items = comments;
            public final double averageRating = avg;
        });
    }
}