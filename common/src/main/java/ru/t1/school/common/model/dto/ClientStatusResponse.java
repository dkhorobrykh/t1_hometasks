package ru.t1.school.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.t1.school.common.model.ClientStatus;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientStatusResponse {
    private UUID clientId;
    private ClientStatus status;
}
