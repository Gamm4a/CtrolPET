package com.example.ClienteRest.controller;

import com.example.ClienteRest.Mapper.Mappers;
import com.example.ClienteRest.dtos.DuenoDTO;
import com.example.ClienteRest.dtos.MascotaDTO;
import com.example.ClienteRest.dtos.ReservaDTO;
import com.example.ctrolpet.exception.ResourceNotFoundException;
import com.example.ctrolpet.exception.UnauthorizedActionException;
import com.example.ctrolpet.model.Dueno;
import com.example.ctrolpet.model.Enum.EstadoReserva;
import com.example.ctrolpet.model.Mascota;
import com.example.ctrolpet.model.Reserva;
import com.example.ctrolpet.service.DuenoService;
import com.example.ctrolpet.service.ReservaService;
import jakarta.servlet.http.HttpSession;
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

    @GetMapping("/{id}")
    public ResponseEntity<DuenoDTO> pantallasDueno(@PathVariable("id") ObjectId idDueno, HttpSession session) throws UnauthorizedActionException, ResourceNotFoundException {

        ObjectId idEnSesion = (ObjectId) session.getAttribute("idDueno");
        if (idEnSesion == null) {
            throw new UnauthorizedActionException("No hay una sesión inciada");
        }

        if (!idEnSesion.equals(idDueno)) {
            throw new UnauthorizedActionException("No tienes acceso a esta cuenta");
        }


        Dueno dueno = duenoService.obtenerPorId(idDueno);
        return ResponseEntity.status(HttpStatus.OK).body(Mappers.toDTO(dueno));
    }

    @PutMapping("/{id}/editar")
    public ResponseEntity<DuenoDTO> editarDueno(@PathVariable("id") ObjectId idDueno, @Valid @RequestBody DuenoDTO duenoActualizado, HttpSession session) {
        ObjectId idEnSesion = (ObjectId) session.getAttribute("idDueno");
        if (idEnSesion == null) {
            throw new UnauthorizedActionException("No hay una sesión inciada");
        }

        if (!idEnSesion.equals(idDueno)) {
            throw new UnauthorizedActionException("No tienes acceso a esta cuenta");
        }

        Dueno dueno = duenoService.obtenerPorId(idDueno);
        dueno = duenoService.actualizarDueno(idDueno ,Mappers.toEntity(duenoActualizado));

        return ResponseEntity.status(HttpStatus.OK).body(Mappers.toDTO(dueno));
    }

    @GetMapping("/{id}/mascotas")
    public ResponseEntity<List<MascotaDTO>> obtenerMacotasDueno(@PathVariable("id")ObjectId idDueno, HttpSession session){

        ObjectId idEnSesion = (ObjectId) session.getAttribute("idDueno");

        if (idEnSesion == null) {
            throw new UnauthorizedActionException("No hay una sesión inciada");
        }

        if (!idEnSesion.equals(idDueno)) {
            throw new UnauthorizedActionException("No tienes acceso a esta cuenta");
        }

        Dueno dueno = duenoService.obtenerPorId(idDueno);


        List<Mascota> mascotas = dueno.getMascotas();
        List<MascotaDTO> mascotasDTO = new ArrayList<>();

        for (Mascota mascota: mascotas){
            mascotasDTO.add(Mappers.toDTO(mascota));
        }

        return ResponseEntity.status(HttpStatus.OK).body(mascotasDTO);

    }

    @PostMapping("/{id}/mascotas/agregar")
    public ResponseEntity<MascotaDTO> crearMacotasDueno(@PathVariable("id")ObjectId idDueno, @Valid @RequestPart("mascota") MascotaDTO mascotaDTO , HttpSession session, @RequestPart("file") MultipartFile file){

        ObjectId idEnSesion = (ObjectId) session.getAttribute("idDueno");

        if (idEnSesion == null) {
            throw new UnauthorizedActionException("No hay una sesión inciada");
        }

        if (!idEnSesion.equals(idDueno)) {
            throw new UnauthorizedActionException("No tienes acceso a esta cuenta");
        }

        Dueno dueno = duenoService.obtenerPorId(idDueno);

        if (dueno == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        duenoService.guardarMascota(idDueno, Mappers.toEntity(mascotaDTO), file);
        Mascota mascotaRegistrada = duenoService.obtenerPorId(idDueno).getMascotas().getLast();
        return ResponseEntity.status(HttpStatus.OK).body(Mappers.toDTO(mascotaRegistrada));

    }

    @GetMapping("/{id}/citas")
    public ResponseEntity<List<ReservaDTO>> obtenerCitasDueno(@PathVariable("id")ObjectId idDueno, HttpSession session){

        ObjectId idEnSesion = (ObjectId) session.getAttribute("idDueno");

        if (idEnSesion == null) {
            throw new UnauthorizedActionException("No hay una sesión inciada");
        }

        if (!idEnSesion.equals(idDueno)) {
            throw new UnauthorizedActionException("No tienes acceso a esta cuenta");
        }

        Dueno dueno = duenoService.obtenerPorId(idDueno);

        if (dueno == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<Reserva> reservas = reservaService.obtenerPorDueno(dueno);
        List<ReservaDTO> reservasDTO = new ArrayList<>();

        for (Reserva reserva: reservas){
            reservasDTO.add(Mappers.toDTO(reserva));
        }

        return ResponseEntity.status(HttpStatus.OK).body(reservasDTO);

    }

    @PutMapping("/{id}/citas/cancelar")
    public ResponseEntity<ReservaDTO> cancelarCita(@PathVariable("id") ObjectId idDueno, @RequestBody Map<String, String> body, HttpSession session) {

        String idReservaStr = body.get("idReserva");
        if (idReservaStr == null || idReservaStr.length() != 24) {
            throw new IllegalArgumentException("El ID de la reserva no es válido.");
        }

        Dueno dueno = duenoService.obtenerPorId(idDueno);
        Reserva reserva = reservaService.obtenerPorId(new ObjectId(idReservaStr));

        if (!dueno.getIdDueno().equals(reserva.getDueno())){
            throw new UnauthorizedActionException("No tienes acceso a esta cuenta");
        }

        Reserva cancelar = new Reserva();
        cancelar.setEstado(EstadoReserva.CANCELADO);

        reserva = reservaService.actualizarParcial(new ObjectId(idReservaStr), cancelar);
        return ResponseEntity.status(HttpStatus.OK).body(Mappers.toDTO(reserva));

    }

    @GetMapping("/{id}/logout")
    public ResponseEntity<?> cerrarSesion(@PathVariable("id")ObjectId idDueno, HttpSession session){

        ObjectId idEnSesion = (ObjectId) session.getAttribute("idDueno");

        if (idEnSesion == null) {
            throw new UnauthorizedActionException("No hay una sesión inciada");
        }

        if (!idEnSesion.equals(idDueno)) {
            throw new UnauthorizedActionException("No tienes acceso a esta cuenta");
        }

        session.invalidate();

        Map<String, String> exito = new HashMap<>();
        exito.put("exito", "Sesión cerrada");

        return ResponseEntity.status(HttpStatus.OK).body(exito);

    }
}
