package ru.t1.school.main_project.model.mapper;

import org.mapstruct.*;
import ru.t1.school.main_project.model.Account;
import ru.t1.school.main_project.model.dto.AccountDto;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {ClientMapper.class})
public interface AccountMapper {
    Account toEntity(AccountDto accountDto);

    AccountDto toDto(Account account);

    List<AccountDto> toDto(List<Account> accounts);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Account partialUpdate(AccountDto accountDto, @MappingTarget Account account);
}