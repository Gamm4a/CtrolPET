package com.example.ClienteRest.controller;

import com.example.ClienteRest.Mapper.Mappers;
import com.example.ClienteRest.dtos.ServicioDTO;
import com.example.ctrolpet.model.Servicio;
import com.example.ctrolpet.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/servicios")
public class ServiciosController {

    @Autowired
    private ServicioService servicioService;

    @GetMapping
    public ResponseEntity<List<ServicioDTO>> obtenerTodos() {
        List<Servicio> servicios = servicioService.obtenerTodos();
        List<ServicioDTO> servicioDTOS = new ArrayList<>();

        for (Servicio s: servicios){
            servicioDTOS.add(Mappers.toDTO(s));
        }

        return ResponseEntity.ok(servicioDTOS);
    }
}
