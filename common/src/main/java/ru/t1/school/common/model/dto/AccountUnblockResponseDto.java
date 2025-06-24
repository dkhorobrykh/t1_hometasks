package ru.t1.school.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountUnblockResponseDto {
    private UUID accountId;
    private Boolean result;
    private String message;
}
