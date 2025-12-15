package dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@NotNull
@Valid
@Size
@AllArgsConstructor
public class AuditDto {

    private String entityType;
    private String operationType;
    private String createdBy;
    private String modifiedBy;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String newEntityJson;
    private String entityJson;

    private Object[] arguments;
    private Object result;

}
