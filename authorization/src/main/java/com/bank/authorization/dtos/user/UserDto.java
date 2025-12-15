package com.bank.authorization.dtos.user;


import com.bank.authorization.utils.validation.ValidUniqueProfile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class UserDto {
    @NotNull(message = "Поле не может быть пустым")
    @Positive(message = "Не может быть отрицательным")
    @ValidUniqueProfile
    private Long profile;
    @NotNull(message = "Поле не может быть пустым")
    private String password;
    @NotBlank
    @Pattern(regexp = "^ROLE_.*", message = "Роли должны начинаться на 'ROLE_'")
    @Size(max = 40, message = "Максимальное количество символов 40")
    private String role;
}
