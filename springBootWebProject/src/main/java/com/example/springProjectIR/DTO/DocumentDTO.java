package com.example.springProjectIR.DTO;

public record DocumentDTO(
        String url,
        String title,
        int score,
        String h1,
        String original_body
) {
}
