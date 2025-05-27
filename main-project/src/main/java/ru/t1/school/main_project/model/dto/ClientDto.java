package ru.t1.school.main_project.model.dto;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link ru.t1.school.main_project.model.Client}
 */
@Data
public class ClientDto implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private UUID clientId;
}