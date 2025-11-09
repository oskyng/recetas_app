package com.duoc.seguridad_calidad.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String cuisineType;
    private String difficulty;
    private String countryOfOrigin;

    @Column(columnDefinition = "TEXT")
    private String ingredients;

    @Column(columnDefinition = "TEXT")
    private String instructions;
}
