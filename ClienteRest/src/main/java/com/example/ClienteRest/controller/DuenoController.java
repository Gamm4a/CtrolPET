package com.example.ClienteRest.controller;

import com.example.ClienteRest.Mapper.Mappers;
import com.example.ClienteRest.dtos.DuenoDTO;
import com.example.ClienteRest.dtos.MascotaDTO;
import com.example.ClienteRest.dtos.ReservaDTO;
import com.example.ClienteRest.services.JwtService;
import com.example.ctrolpet.exception.ResourceNotFoundException;
import com.example.ctrolpet.exception.UnauthorizedActionException;
import com.example.ctrolpet.model.Dueno;
import com.example.ctrolpet.model.Enum.EstadoReserva;
import com.example.ctrolpet.model.Mascota;
import com.example.ctrolpet.model.Reserva;
import com.example.ctrolpet.service.DuenoService;
import com.example.ctrolpet.service.ReservaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dueno")
public class DuenoController {

    @Autowired
    private DuenoService duenoService;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private JwtService jwtService;


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

    @GetMapping("/{id}")
    public ResponseEntity<DuenoDTO> pantallasDueno(@PathVariable("id") ObjectId idDueno, HttpServletRequest request) {
        Dueno dueno = validarAccesoDueno(idDueno, request);
        return ResponseEntity.status(HttpStatus.OK).body(Mappers.toDTO(dueno));
    }

    @PatchMapping("/{id}/editar")
    public ResponseEntity<DuenoDTO> editarDueno(@PathVariable("id") ObjectId idDueno, @RequestBody DuenoDTO duenoActualizado, HttpServletRequest request) {
        validarAccesoDueno(idDueno, request);

        Dueno dueno = duenoService.actualizarDueno(idDueno, Mappers.toEntityUpdate(duenoActualizado));
        return ResponseEntity.status(HttpStatus.OK).body(Mappers.toDTO(dueno));
    }

    @GetMapping("/{id}/mascotas")
    public ResponseEntity<List<MascotaDTO>> obtenerMacotasDueno(@PathVariable("id") ObjectId idDueno, HttpServletRequest request) {
        Dueno dueno = validarAccesoDueno(idDueno, request);

        List<Mascota> mascotas = dueno.getMascotas();
        List<MascotaDTO> mascotasDTO = new ArrayList<>();
        if (mascotas != null) {
            for (Mascota mascota : mascotas) {
                mascotasDTO.add(Mappers.toDTO(mascota));
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(mascotasDTO);
    }

    @PostMapping("/{id}/mascotas/agregar")
    public ResponseEntity<MascotaDTO> crearMacotasDueno(@PathVariable("id") ObjectId idDueno, @Valid @RequestPart("mascota") MascotaDTO mascotaDTO, @RequestPart("file") MultipartFile file, HttpServletRequest request) {
        validarAccesoDueno(idDueno, request);

        duenoService.guardarMascota(idDueno, Mappers.toEntity(mascotaDTO), file);
        Mascota mascotaRegistrada = duenoService.obtenerPorId(idDueno).getMascotas().getLast();
        return ResponseEntity.status(HttpStatus.OK).body(Mappers.toDTO(mascotaRegistrada));
    }

    @GetMapping("/{id}/citas")
    public ResponseEntity<List<ReservaDTO>> obtenerCitasDueno(@PathVariable("id") ObjectId idDueno, HttpServletRequest request) {
        Dueno dueno = validarAccesoDueno(idDueno, request);

        List<Reserva> reservas = reservaService.obtenerPorDueno(dueno);
        List<ReservaDTO> reservasDTO = new ArrayList<>();
        if (reservas != null) {
            for (Reserva reserva : reservas) {
                reservasDTO.add(Mappers.toDTO(reserva));
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(reservasDTO);
    }

    @PutMapping("/{id}/citas/cancelar")
    public ResponseEntity<ReservaDTO> cancelarCita(@PathVariable("id") ObjectId idDueno, @RequestBody Map<String, String> body, HttpServletRequest request) {
        validarAccesoDueno(idDueno, request);

        String idReservaStr = body.get("idReserva");
        if (idReservaStr == null || idReservaStr.length() != 24) {
            throw new IllegalArgumentException("El ID de la reserva no es válido.");
        }

        Reserva reserva = reservaService.obtenerPorId(new ObjectId(idReservaStr));

        if (!idDueno.equals(reserva.getDueno())) {
            throw new UnauthorizedActionException("Esta reserva no pertenece a este usuario");
        }

        Reserva cancelar = new Reserva();
        cancelar.setEstado(EstadoReserva.CANCELADO);

        reserva = reservaService.actualizarParcial(new ObjectId(idReservaStr), cancelar);
        return ResponseEntity.status(HttpStatus.OK).body(Mappers.toDTO(reserva));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> cerrarSesion(@RequestHeader("Authorization") String tokenHeader){

        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "No se proporcionó un token válido");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        // Extraemos el token puro (quitando "Bearer ")
        String token = tokenHeader.substring(7);

        // Lo mandamos a la lista negra de tu JwtService
        jwtService.invalidateToken(token);

        Map<String, String> exito = new HashMap<>();
        exito.put("exito", "Sesión cerrada correctamente. Token invalidado.");

        return ResponseEntity.status(HttpStatus.OK).body(exito);
    }


}

