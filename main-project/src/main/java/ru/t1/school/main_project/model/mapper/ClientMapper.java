package ru.t1.school.main_project.model.mapper;

import org.mapstruct.*;
import ru.t1.school.main_project.model.Client;
import ru.t1.school.main_project.model.dto.ClientDto;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClientMapper {
    Client toEntity(ClientDto clientDto);

    ClientDto toDto(Client client);

    List<ClientDto> toDto(List<Client> clients);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Client partialUpdate(ClientDto clientDto, @MappingTarget Client client);
}