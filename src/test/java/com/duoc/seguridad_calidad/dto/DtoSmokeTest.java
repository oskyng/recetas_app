package com.duoc.seguridad_calidad.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DtoSmokeTest {

    @Test
    void addMediaRequest_gettersSetters() {
        AddMediaRequest req = new AddMediaRequest();
        req.setUrl("http://x");
        req.setType("IMG");
        assertThat(req.getUrl()).isEqualTo("http://x");
        assertThat(req.getType()).isEqualTo("IMG");
    }

    @Test
    void commentRequest_gettersSetters() {
        CommentRequest req = new CommentRequest();
        req.setText("Nice");
        req.setRating(5);
        assertThat(req.getText()).isEqualTo("Nice");
        assertThat(req.getRating()).isEqualTo(5);
    }

    @Test
    void shareResponse_constructorAndGetters() {
        ShareResponse resp = new ShareResponse("msg", "/recipes/1");
        assertThat(resp.getMessage()).isEqualTo("msg");
        assertThat(resp.getShareUrl()).isEqualTo("/recipes/1");
    }
}
