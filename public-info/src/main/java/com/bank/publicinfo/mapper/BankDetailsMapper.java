package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.model.BankDetailsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, 
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BankDetailsMapper {

    BankDetailsDto toDto(BankDetailsEntity entity);

    BankDetailsEntity toEntity(BankDetailsDto dto);

    void updateEntityFromDto(BankDetailsDto dto, @MappingTarget BankDetailsEntity entity);
}
