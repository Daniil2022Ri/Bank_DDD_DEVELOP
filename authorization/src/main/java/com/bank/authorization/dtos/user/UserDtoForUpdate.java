package com.bank.authorization.dtos.user;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDtoForUpdate {
    @Positive(message = "Не может быть отрицательным")
    private Long profile;
    private String password;
    @Pattern(regexp = "^ROLE_.*", message = "Роли должны начинаться на 'ROLE_'")
    @Size(max = 40, message = "Максимальное количество символов 40")
    private String role;
}
