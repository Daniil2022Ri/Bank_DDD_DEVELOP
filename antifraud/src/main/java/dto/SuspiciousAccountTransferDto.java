package dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NotNull
@Valid
@Size
@AllArgsConstructor
@NoArgsConstructor
public class SuspiciousAccountTransferDto {

    private Long id;
    @NotNull(message = "Поле blocked не может быть null")
    private boolean blocked;
    @NotNull(message = "Поле suspicious не может быть null")
    private boolean suspicious;
    @NotBlank(message = "Причина блокировки не может быть пустой")
    private String blockedReason;
    @NotBlank(message = "Подозрительная причина не может быть пустой")
    private String suspiciousReason;

}
