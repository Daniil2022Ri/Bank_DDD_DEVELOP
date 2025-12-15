package mappers;

import dto.SuspiciousPhoneTransferDto;
import model.SuspiciousPhoneTransfer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SuspiciousPhoneTransferMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "blocked", source = "blocked")
    @Mapping(target = "suspicious", source = "suspicious")
    @Mapping(target = "blockedReason", source = "blockedReason")
    @Mapping(target = "suspiciousReason", source = "suspiciousReason")
    SuspiciousPhoneTransferDto toDto(SuspiciousPhoneTransfer entity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "blocked", source = "blocked")
    @Mapping(target = "suspicious", source = "suspicious")
    @Mapping(target = "blockedReason", source = "blockedReason")
    @Mapping(target = "suspiciousReason", source = "suspiciousReason")
    SuspiciousPhoneTransfer toEntity(SuspiciousPhoneTransferDto dto);

}
