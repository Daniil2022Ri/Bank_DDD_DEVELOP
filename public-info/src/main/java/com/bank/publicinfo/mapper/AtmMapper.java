package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.AtmDto;
import com.bank.publicinfo.model.AtmEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AtmMapper {

    @Mapping(target = "bankDetailsId", source = "bankDetails.id")
    AtmDto toDto(AtmEntity entity);

    @Mapping(target = "bankDetails", ignore = true)
    AtmEntity toEntity(AtmDto dto);

    @Mapping(target = "bankDetails", ignore = true)
    void updateEntityFromDto(AtmDto dto, @MappingTarget AtmEntity entity);
}
