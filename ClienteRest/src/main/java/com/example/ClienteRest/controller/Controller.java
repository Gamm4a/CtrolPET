package com.example.ClienteRest.controller;




import com.example.ClienteRest.Mapper.Mapper;
import com.example.ClienteRest.dtos.*;
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
@RequestMapping("/api")
public class Controller {

    @Autowired
    private DuenoService duenoService;

    @Autowired
    private ReservaService reservaService;


    @PostMapping("/registro")
    public ResponseEntity<DuenoDTO> registrarDueno(@Valid @RequestBody RegistroDTO registroDTO) {

        Dueno dueno = Mapper.toEntity(registroDTO.getDuenoDTO());

        Dueno duenoRegistrado = duenoService.guardar(dueno);

        if (registroDTO.getReservaDTO() != null && duenoRegistrado.getMascotas() != null && !duenoRegistrado.getMascotas().isEmpty()) {
            Reserva reserva = Mapper.toEntity(registroDTO.getReservaDTO());
            reserva.setMascota(duenoRegistrado.getMascotas().getFirst().getIdMascota());
            reserva.setDueno(duenoRegistrado.getIdDueno());
            reservaService.guardar(reserva);
        }


        return ResponseEntity.status(HttpStatus.CREATED).body(Mapper.toDTO(duenoRegistrado));
    }


    @PostMapping("/reservas")
    public ResponseEntity<ReservaDTO> crearReservaSinLogin(@Valid @RequestBody ReservaDTO reservaDTO) {

        Reserva guardada = reservaService.guardar(Mapper.toEntity(reservaDTO));

        return ResponseEntity.status(HttpStatus.CREATED).body(Mapper.toDTO(guardada));

    }


    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesionDueno(@Valid @RequestBody LoginRequestDTO loginRequestDTO, HttpSession session) {

        Dueno duenoRegistrado = duenoService.autenticar(loginRequestDTO.getCorreo(), loginRequestDTO.getContrasenia());


        session.setAttribute("idDueno", duenoRegistrado.getIdDueno());
        return ResponseEntity.status(HttpStatus.OK).body(Mapper.toDTO(duenoRegistrado));



    }


    @GetMapping({"/{id}", "/perfil/{id}"})
    public ResponseEntity<DuenoDTO> pantallasDueno(@PathVariable("id") ObjectId idDueno, HttpSession session) throws UnauthorizedActionException, ResourceNotFoundException {

        ObjectId idEnSesion = (ObjectId) session.getAttribute("idDueno");

        if (idEnSesion == null) {

            throw new UnauthorizedActionException("No hay una sesión inciada");

        }

        if (!idEnSesion.equals(idDueno)) {

            throw new UnauthorizedActionException("No tienes acceso a esta cuenta");

        }


        Dueno dueno = duenoService.obtenerPorId(idDueno);


        return ResponseEntity.status(HttpStatus.OK).body(Mapper.toDTO(dueno));
    }

    @PutMapping("/perfil/{id}/editar")
    public ResponseEntity<DuenoDTO> editarDueno(@PathVariable("id") ObjectId idDueno, @Valid @RequestBody DuenoDTO duenoActualizado, HttpSession session) {

        ObjectId idEnSesion = (ObjectId) session.getAttribute("idDueno");

        if (idEnSesion == null) {

            throw new UnauthorizedActionException("No hay una sesión inciada");

        }

        if (!idEnSesion.equals(idDueno)) {

            throw new UnauthorizedActionException("No tienes acceso a esta cuenta");

        }

        Dueno dueno = duenoService.obtenerPorId(idDueno);


        dueno = duenoService.actualizarDueno(idDueno ,Mapper.toEntity(duenoActualizado));


        return ResponseEntity.status(HttpStatus.OK).body(Mapper.toDTO(dueno));
    }

    @GetMapping("/perfil/{id}/mascotas")
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

            mascotasDTO.add(Mapper.toDTO(mascota));

        }

        return ResponseEntity.status(HttpStatus.OK).body(mascotasDTO);

    }

    @PostMapping("/perfil/{id}/mascotas/agregar")
    public ResponseEntity<MascotaDTO> crearMacotasDueno(@PathVariable("id")ObjectId idDueno, @Valid @RequestPart("mascota") MascotaDTO mascotaDTO , HttpSession session, @RequestPart("file") MultipartFile file){

        ObjectId idEnSesion = (ObjectId) session.getAttribute("idDueno");

        if (idEnSesion == null) {

            throw new UnauthorizedActionException("No hay una sesión inciada");

        }

        if (!idEnSesion.equals(idDueno)) {

            throw new UnauthorizedActionException("No tienes acceso a esta cuenta");

        }

        Dueno dueno = duenoService.obtenerPorId(idDueno);


        duenoService.guardarMascota(idDueno, Mapper.toEntity(mascotaDTO), file);

        Mascota mascotaRegistrada = duenoService.obtenerPorId(idDueno).getMascotas().getLast();


        return ResponseEntity.status(HttpStatus.OK).body(Mapper.toDTO(mascotaRegistrada));

    }

    @GetMapping("/perfil/{id}/citas")
    public ResponseEntity<List<ReservaDTO>> obtenerCitasDueno(@PathVariable("id")ObjectId idDueno, HttpSession session){

        ObjectId idEnSesion = (ObjectId) session.getAttribute("idDueno");

        if (idEnSesion == null) {

            throw new UnauthorizedActionException("No hay una sesión inciada");

        }

        if (!idEnSesion.equals(idDueno)) {

            throw new UnauthorizedActionException("No tienes acceso a esta cuenta");

        }

        Dueno dueno = duenoService.obtenerPorId(idDueno);


        List<Reserva> reservas = reservaService.obtenerPorDueno(dueno);
        List<ReservaDTO> reservasDTO = new ArrayList<>();

        for (Reserva reserva: reservas){

            reservasDTO.add(Mapper.toDTO(reserva));

        }



        return ResponseEntity.status(HttpStatus.OK).body(reservasDTO);

    }

    @PutMapping("/perfil/{id}/citas/cancelar")
    public ResponseEntity<ReservaDTO> cancelarCita(@PathVariable("id") ObjectId idDueno, @RequestBody Map<String, String> body, HttpSession session) {

        // ... Conservas tus validaciones de sesión idEnSesion ...

        // Extraes el string limpio desde el JSON
        String idReservaStr = body.get("idReserva");
        if (idReservaStr == null || idReservaStr.length() != 24) {
            throw new IllegalArgumentException("El ID de la reserva no es válido.");
        }

        Dueno dueno = duenoService.obtenerPorId(idDueno);

        // Ahora sí, usas el String limpio para crear el ObjectId
        Reserva reserva = reservaService.obtenerPorId(new ObjectId(idReservaStr));

        if (!dueno.getIdDueno().equals(reserva.getDueno())){
            throw new UnauthorizedActionException("No tienes acceso a esta cuenta");
        }

        Reserva cancelar = new Reserva();
        cancelar.setEstado(EstadoReserva.CANCELADO);

        reserva = reservaService.actualizarParcial(new ObjectId(idReservaStr), cancelar);

        return ResponseEntity.status(HttpStatus.OK).body(Mapper.toDTO(reserva));
    }

    @GetMapping("/perfil/{id}/logout")
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


    @PostMapping("/reservas/dueno/{id}")
    public ResponseEntity<ReservaDTO> crearReservaConLogin(@PathVariable("id")ObjectId idDueno,@Valid @RequestBody ReservaDTO reservaDTO, HttpSession session) {

        ObjectId idEnSesion = (ObjectId) session.getAttribute("idDueno");

        if (idEnSesion == null) {

            throw new UnauthorizedActionException("No hay una sesión inciada");

        }

        if (!idEnSesion.equals(idDueno)) {

            throw new UnauthorizedActionException("No tienes acceso a esta cuenta");

        }

        Reserva guardada = reservaService.guardar(Mapper.toEntity(reservaDTO));

        return ResponseEntity.status(HttpStatus.CREATED).body(Mapper.toDTO(guardada));

    }

}
