package org.kharitonov.ms_jwt_auth.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignInRequest(
        @NotBlank
        @Size(min = 2, max = 50, message = "Имя пользователя должно быть не мене 2 и не более 50 символов")
        String username,
        @NotBlank
        @Size(min = 2, max = 50, message = "Пароль пользователя должен быть не мене 2 и не более 50 символов")
        String password
) {
}
