package com.example.ClienteRest.dtos;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroDTO {

    @Valid
    private DuenoDTO duenoDTO;

    @Valid
    private ReservaDTO reservaDTO;

}
