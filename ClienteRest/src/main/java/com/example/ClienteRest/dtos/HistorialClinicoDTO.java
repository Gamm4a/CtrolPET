package com.example.ClienteRest.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistorialClinicoDTO {

    private String idMascota;
    private Instant fecha;
    private String diagnostico;
    private String observaciones;
    private List<MedicamentoDTO> medicamentos;


}
