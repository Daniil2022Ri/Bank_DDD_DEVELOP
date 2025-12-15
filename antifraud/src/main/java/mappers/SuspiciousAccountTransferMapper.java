package mappers;

import dto.SuspiciousAccountTransferDto;
import model.SuspiciousAccountTransfer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SuspiciousAccountTransferMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "blocked", source = "blocked")
    @Mapping(target = "suspicious", source = "suspicious")
    @Mapping(target = "blockedReason", source = "blockedReason")
    @Mapping(target = "suspiciousReason", source = "suspiciousReason")
    SuspiciousAccountTransferDto toDto(SuspiciousAccountTransfer entity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "blocked", source = "blocked")
    @Mapping(target = "suspicious", source = "suspicious")
    @Mapping(target = "blockedReason", source = "blockedReason")
    @Mapping(target = "suspiciousReason", source = "suspiciousReason")
    SuspiciousAccountTransfer toEntity(SuspiciousAccountTransferDto dto);

}
