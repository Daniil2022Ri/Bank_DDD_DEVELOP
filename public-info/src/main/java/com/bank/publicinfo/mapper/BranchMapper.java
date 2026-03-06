package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.BranchDto;
import com.bank.publicinfo.model.BranchEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BranchMapper {

    @Mapping(target = "bankDetailsId", source = "bankDetails.id")
    BranchDto toDto(BranchEntity entity);

    @Mapping(target = "bankDetails", ignore = true)
    BranchEntity toEntity(BranchDto dto);

    @Mapping(target = "bankDetails", ignore = true)
    void updateEntityFromDto(BranchDto dto, @MappingTarget BranchEntity entity);
}
