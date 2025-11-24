package com.duoc.seguridad_calidad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShareResponse {
    private String message;
    private String shareUrl;
}
