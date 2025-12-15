package com.bank.account.mapper;

import com.bank.account.dto.AuditDto;
import com.bank.account.entity.AuditEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuditMapper {
    AuditDto toDto(AuditEntity entity);
    AuditEntity toEntity(AuditDto dto);
}


