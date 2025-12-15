package mappers;

import dto.SuspiciousCardTransferDto;
import model.SuspiciousCardTransfer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SuspiciousCardTransferMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "blocked", source = "blocked")
    @Mapping(target = "suspicious", source = "suspicious")
    @Mapping(target = "blockedReason", source = "blockedReason")
    @Mapping(target = "suspiciousReason", source = "suspiciousReason")
    SuspiciousCardTransferDto toDto(SuspiciousCardTransfer entity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "blocked", source = "blocked")
    @Mapping(target = "suspicious", source = "suspicious")
    @Mapping(target = "blockedReason", source = "blockedReason")
    @Mapping(target = "suspiciousReason", source = "suspiciousReason")
    SuspiciousCardTransfer toEntity(SuspiciousCardTransferDto dto);

}
