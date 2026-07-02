package com.example.ClienteRest.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.ClienteRest.dtos.Enum.EspecialidadDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicioDTO {

    private String tipo;
    private String descripcion;
    private Double precio;
    private EspecialidadDTO categoria;
    private Integer duracion;

}
