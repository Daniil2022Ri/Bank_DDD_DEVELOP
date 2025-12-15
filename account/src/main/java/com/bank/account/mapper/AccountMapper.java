package com.bank.account.mapper;

import com.bank.account.dto.AccountDto;
import com.bank.account.entity.AccountEntity;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDto toDto(AccountEntity entity);
    AccountEntity toEntity(AccountDto dto);
    List<AccountDto> toDtoList(List<AccountEntity> entities);
}

