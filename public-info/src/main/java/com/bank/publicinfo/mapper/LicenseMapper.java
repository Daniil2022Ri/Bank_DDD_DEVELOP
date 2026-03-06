package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.LicenseDto;
import com.bank.publicinfo.model.LicenseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LicenseMapper {

    @Mapping(target = "bankDetailsId", source = "bankDetails.id")
    LicenseDto toDto(LicenseEntity entity);

    @Mapping(target = "bankDetails", ignore = true)
    LicenseEntity toEntity(LicenseDto dto);

    @Mapping(target = "bankDetails", ignore = true)
    void updateEntityFromDto(LicenseDto dto, @MappingTarget LicenseEntity entity);
}
