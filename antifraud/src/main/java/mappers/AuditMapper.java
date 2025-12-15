package mappers;

import dto.AuditDto;
import model.Audit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuditMapper {

    AuditDto toDto(Audit audit);
    Audit toEntity(AuditDto dto);

}
