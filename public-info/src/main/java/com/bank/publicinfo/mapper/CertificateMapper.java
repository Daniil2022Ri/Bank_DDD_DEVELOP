package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.CertificateDto;
import com.bank.publicinfo.model.CertificateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CertificateMapper {

    @Mapping(target = "bankDetailsId", source = "bankDetails.id")
    CertificateDto toDto(CertificateEntity entity);

    @Mapping(target = "bankDetails", ignore = true)
    CertificateEntity toEntity(CertificateDto dto);

    @Mapping(target = "bankDetails", ignore = true)
    void updateEntityFromDto(CertificateDto dto, @MappingTarget CertificateEntity entity);
}
