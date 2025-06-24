package ru.t1.school.common.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.t1.school.common.model.ClientStatus;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientUnblockRequestDto {
    @NotNull
    private UUID clientId;

    @NotNull
    private ClientStatus clientStatus;
}
