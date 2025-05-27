package ru.t1.school.main_project.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link ru.t1.school.main_project.model.Client}
 */
@Data
public class AddClientDto implements Serializable {
    @NotEmpty
    @Size(max = 255)
    private String firstName;
    @NotEmpty
    @Size(max = 255)
    private String lastName;
    @Size(max = 255)
    private String middleName;
}