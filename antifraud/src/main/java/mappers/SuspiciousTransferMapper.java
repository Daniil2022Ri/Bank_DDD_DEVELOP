package mappers;

import dto.SuspiciousAccountTransferDto;
import dto.SuspiciousCardTransferDto;
import dto.SuspiciousPhoneTransferDto;
import model.SuspiciousAccountTransfer;
import model.SuspiciousCardTransfer;
import model.SuspiciousPhoneTransfer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SuspiciousTransferMapper {

    SuspiciousCardTransferDto toCardDto(SuspiciousCardTransfer entity);
    SuspiciousCardTransfer toCardEntity(SuspiciousCardTransferDto dto);

    SuspiciousPhoneTransferDto toPhoneDto(SuspiciousPhoneTransfer entity);
    SuspiciousPhoneTransfer toPhoneEntity(SuspiciousPhoneTransferDto dto);

    SuspiciousAccountTransferDto toAccountDto(SuspiciousAccountTransfer entity);
    SuspiciousAccountTransfer toAccountEntity(SuspiciousAccountTransferDto dto);

}