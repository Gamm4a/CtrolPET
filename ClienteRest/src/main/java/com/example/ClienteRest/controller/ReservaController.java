package com.example.ClienteRest.controller;

import com.example.ClienteRest.Mapper.Mappers;
import com.example.ClienteRest.dtos.ReservaDTO;
import com.example.ClienteRest.services.JwtService;
import com.example.ctrolpet.exception.ResourceNotFoundException;
import com.example.ctrolpet.exception.UnauthorizedActionException;
import com.example.ctrolpet.model.Dueno;
import com.example.ctrolpet.model.Reserva;
import com.example.ctrolpet.service.DuenoService;
import com.example.ctrolpet.service.ReservaService;
import jakarta.servlet.http.HttpServletRequest;
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

    @Autowired
    private DuenoService duenoService;

    private Dueno validarAccesoDueno(ObjectId idDueno, HttpServletRequest request) {
        String emailEnToken = (String) request.getAttribute("email");
        Dueno dueno = duenoService.obtenerPorId(idDueno);

        if (dueno == null) {
            throw new ResourceNotFoundException("El dueño no existe");
        }


        if (!dueno.getCorreo().equals(emailEnToken)) {
            throw new UnauthorizedActionException("No tienes acceso a esta cuenta");
        }

        return dueno;
    }

    @PostMapping
    public ResponseEntity<ReservaDTO> crearReservaSinLogin(@Valid @RequestBody ReservaDTO reservaDTO) {
        Reserva guardada = reservaService.guardar(Mappers.toEntity(reservaDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(Mappers.toDTO(guardada));
    }

    @PostMapping("/dueno/{id}")
    public ResponseEntity<ReservaDTO> crearReservaConLogin(@PathVariable("id") ObjectId idDueno, @Valid @RequestBody ReservaDTO reservaDTO, HttpServletRequest request) {

        validarAccesoDueno(idDueno, request);

        Reserva guardada = reservaService.guardar(Mappers.toEntity(reservaDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(Mappers.toDTO(guardada));
    }
}
