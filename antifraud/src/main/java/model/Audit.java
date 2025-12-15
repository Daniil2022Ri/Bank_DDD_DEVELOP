package model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "anti_fraud")
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( name = "entity_type" , length = 40)
    private String entityType;

    @Column(name = "operation_type" , length = 255)
    private String operationType;

    @Column(name = "created_by" , length = 255)
    private String createdBy;

    @Column(name = "modified_by" , length = 255)
    private String modifiedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "new_entity_json")
    private String newEntityJson;

    @Column(name = "entity_json")
    private String entityJson;


}
