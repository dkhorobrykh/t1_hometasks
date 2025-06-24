package ru.t1.school.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientUnblockResponseDto {
    private UUID clientId;
    private Boolean result;
    private String message;
}
