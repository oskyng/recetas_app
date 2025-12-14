package com.duoc.seguridad_calidad.web;

import com.duoc.seguridad_calidad.controllers.RecipeApiController;
import com.duoc.seguridad_calidad.dto.AddMediaRequest;
import com.duoc.seguridad_calidad.dto.CommentRequest;
import com.duoc.seguridad_calidad.dto.ShareResponse;
import com.duoc.seguridad_calidad.model.Recipe;
import com.duoc.seguridad_calidad.model.RecipeComment;
import com.duoc.seguridad_calidad.model.RecipeMedia;
import com.duoc.seguridad_calidad.service.RecipeCommentService;
import com.duoc.seguridad_calidad.service.RecipeMediaService;
import com.duoc.seguridad_calidad.service.ShareService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class RecipeApiControllerTest {

    private RecipeMediaService mediaService;
    private RecipeCommentService commentService;
    private ShareService shareService;
    private RecipeApiController controller;

    private final Authentication auth = new UsernamePasswordAuthenticationToken("alice", "N/A");

    @BeforeEach
    void setup() {
        mediaService = Mockito.mock(RecipeMediaService.class);
        commentService = Mockito.mock(RecipeCommentService.class);
        shareService = Mockito.mock(ShareService.class);
        controller = new RecipeApiController(mediaService, commentService, shareService);
    }

    @Test
    void addMedia_returns404_whenRecipeMissing() {
        when(mediaService.findRecipe(99L)).thenReturn(Optional.empty());
        ResponseEntity<?> resp = controller.addMedia(99L, new AddMediaRequest(), auth);
        assertThat(resp.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void addMedia_returns201_andBody_whenOk() {
        Recipe r = Recipe.builder().id(1L).name("R").build();
        when(mediaService.findRecipe(1L)).thenReturn(Optional.of(r));
        RecipeMedia saved = RecipeMedia.builder().id(10L).url("u").type("IMG").username("alice").recipe(r).build();
        when(mediaService.addMedia(r, "u", "IMG", "alice")).thenReturn(saved);

        AddMediaRequest req = new AddMediaRequest();
        req.setUrl("u");
        req.setType("IMG");
        ResponseEntity<?> resp = controller.addMedia(1L, req, auth);

        assertThat(resp.getStatusCode().value()).isEqualTo(201);
        assertThat(resp.getHeaders().getLocation()).isEqualTo(URI.create("/api/recipes/1/media/10"));
        assertThat(resp.getBody()).isInstanceOf(RecipeMedia.class);
    }

    @Test
    void listMedia_returns404_whenMissing() {
        when(mediaService.findRecipe(77L)).thenReturn(Optional.empty());
        ResponseEntity<?> resp = controller.listMedia(77L);
        assertThat(resp.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void listMedia_returns200_withList() {
        Recipe r = Recipe.builder().id(3L).build();
        when(mediaService.findRecipe(3L)).thenReturn(Optional.of(r));
        when(mediaService.listByRecipe(r)).thenReturn(List.of(RecipeMedia.builder().id(1L).recipe(r).build()));
        ResponseEntity<?> resp = controller.listMedia(3L);
        assertThat(resp.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void share_returns404_whenMissing() {
        when(mediaService.findRecipe(5L)).thenReturn(Optional.empty());
        ResponseEntity<?> resp = controller.share(5L, auth);
        assertThat(resp.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void share_returns200_withShareResponse() {
        Recipe r = Recipe.builder().id(2L).name("Cake").build();
        when(mediaService.findRecipe(2L)).thenReturn(Optional.of(r));
        when(shareService.buildSiteUrl(r)).thenReturn("/recipes/2");
        when(shareService.buildSocialMessage(r, "alice")).thenReturn("msg");
        ResponseEntity<?> resp = controller.share(2L, auth);
        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        assertThat(resp.getBody()).isInstanceOf(ShareResponse.class);
    }

    @Test
    void addComment_returns400_whenInvalidRating() {
        CommentRequest req = new CommentRequest();
        req.setText("x");
        req.setRating(0);
        ResponseEntity<?> resp = controller.addComment(1L, req, auth);
        assertThat(resp.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void addComment_returns404_whenRecipeMissing() {
        CommentRequest req = new CommentRequest();
        req.setText("x");
        req.setRating(5);
        when(commentService.findRecipe(9L)).thenReturn(Optional.empty());
        ResponseEntity<?> resp = controller.addComment(9L, req, auth);
        assertThat(resp.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void addComment_returns200_withSaved() {
        Recipe r = Recipe.builder().id(7L).build();
        when(commentService.findRecipe(7L)).thenReturn(Optional.of(r));
        RecipeComment saved = RecipeComment.builder().id(100L).text("ok").rating(4).username("alice").recipe(r).build();
        when(commentService.addComment(r, "ok", 4, "alice")).thenReturn(saved);

        CommentRequest req = new CommentRequest();
        req.setText("ok");
        req.setRating(4);
        ResponseEntity<?> resp = controller.addComment(7L, req, auth);
        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        assertThat(resp.getBody()).isInstanceOf(RecipeComment.class);
    }

    @Test
    void listComments_returns404_whenMissing() {
        when(commentService.findRecipe(4L)).thenReturn(Optional.empty());
        ResponseEntity<?> resp = controller.listComments(4L);
        assertThat(resp.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void listComments_returns200_withItemsAndAverage() {
        Recipe r = Recipe.builder().id(8L).build();
        when(commentService.findRecipe(8L)).thenReturn(Optional.of(r));
        when(commentService.listByRecipe(r)).thenReturn(List.of());
        when(commentService.getAverageRating(r)).thenReturn(0.0);
        ResponseEntity<?> resp = controller.listComments(8L);
        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        assertThat(resp.getBody()).isNotNull();
    }
}
