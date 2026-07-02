package com.example.ClienteRest.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroDTO {

    private DuenoDTO duenoDTO;

    private ReservaDTO reservaDTO;

}
