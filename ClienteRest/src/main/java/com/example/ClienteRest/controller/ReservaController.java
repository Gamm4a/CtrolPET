package com.example.ClienteRest.controller;

import com.example.ClienteRest.Mapper.Mappers;
import com.example.ClienteRest.dtos.ReservaDTO;
import com.example.ctrolpet.exception.UnauthorizedActionException;
import com.example.ctrolpet.model.Reserva;
import com.example.ctrolpet.service.ReservaService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @PostMapping
    public ResponseEntity<ReservaDTO> crearReservaSinLogin(@Valid @RequestBody ReservaDTO reservaDTO) {
        Reserva guardada = reservaService.guardar(Mappers.toEntity(reservaDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(Mappers.toDTO(guardada));
    }

    @PostMapping("/dueno/{id}")
    public ResponseEntity<ReservaDTO> crearReservaConLogin(@PathVariable("id") ObjectId idDueno, @Valid @RequestBody ReservaDTO reservaDTO, HttpSession session) {

        ObjectId idEnSesion = (ObjectId) session.getAttribute("idDueno");

        if (idEnSesion == null) {
            throw new UnauthorizedActionException("No hay una sesión inciada");
        }

        if (!idEnSesion.equals(idDueno)) {
            throw new UnauthorizedActionException("No tienes acceso a esta cuenta");
        }

        Reserva guardada = reservaService.guardar(Mappers.toEntity(reservaDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(Mappers.toDTO(guardada));
    }
}
