package com.example.springProjectIR.Model;

import java.time.LocalDateTime;

public record Account(
        String username,
        String password,
        String salt,
        String email,
        LocalDateTime date_created,
        LocalDateTime date_updated
) {
}
