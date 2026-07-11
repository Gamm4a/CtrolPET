package com.example.ClienteRest.controller;

import com.example.ClienteRest.Mapper.Mappers;
import com.example.ClienteRest.dtos.SucursalDTO;
import com.example.ctrolpet.model.Sucursal;
import com.example.ctrolpet.service.SucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
public class SucursalController {

    @Autowired
    private SucursalService sucursalService;

    @GetMapping
    public ResponseEntity<List<SucursalDTO>> obtenerTodos() {
        List<Sucursal> sucursales = sucursalService.obtenerTodos();
        List<SucursalDTO> sucursalDTOS = new ArrayList<>();

        for (Sucursal s: sucursales){
            sucursalDTOS.add(Mappers.toDTO(s));
        }

        return ResponseEntity.ok(sucursalDTOS);
    }
}
