package com.example.ClienteRest.controller;




import com.example.ClienteRest.dtos.LoginRequestDTO;
import com.example.ClienteRest.dtos.RegistroDTO;
import com.example.ctrolpet.model.Dueno;
import com.example.ctrolpet.model.Mascota;
import com.example.ctrolpet.model.Reserva;
import com.example.ctrolpet.service.DuenoService;
import com.example.ctrolpet.service.ReservaService;

import jakarta.servlet.http.HttpSession;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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
    public ResponseEntity<Dueno> registrarDueno(@RequestBody RegistroDTO registroDTO) {
        Dueno dueno = registroDTO.getDuenoDTO();
        Reserva reserva = registroDTO.getReservaDTO();

        Dueno duenoRegistrado = duenoService.guardar(dueno);

        if (reserva != null && duenoRegistrado.getMascotas() != null && !duenoRegistrado.getMascotas().isEmpty()) {
            reserva.setMascota(duenoRegistrado.getMascotas().getFirst().getIdMascota());
            reserva.setDueno(duenoRegistrado.getIdDueno());
            reservaService.guardar(reserva);
        }


        return ResponseEntity.status(HttpStatus.CREATED).body(duenoRegistrado);
    }


    @PostMapping("/reservas")
    public ResponseEntity<Reserva> crearReservaSinLogin(@RequestBody Reserva reserva) {
        Reserva guardada = reservaService.guardar(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }


    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesionDueno(@RequestBody LoginRequestDTO loginRequestDTO, HttpSession session) {
        Dueno duenoRegistrado = duenoService.autenticar(loginRequestDTO.getCorreo(), loginRequestDTO.getContrasenia());

        if (duenoRegistrado != null) {

            session.setAttribute("idDueno", duenoRegistrado.getIdDueno());


            return ResponseEntity.status(HttpStatus.OK).body(duenoRegistrado);

        }


        Map<String, String> error = new HashMap<>();
        error.put("error", "Credenciales Incorrectas");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }


    @GetMapping({"/{id}", "/perfil/{id}"})
    public ResponseEntity<Dueno> pantallasDueno(@PathVariable("id") ObjectId idDueno, HttpSession session) {

        ObjectId idEnSesion = (ObjectId) session.getAttribute("idDueño");

        if (idEnSesion == null) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!idEnSesion.equals(idDueno)) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


        Dueno dueno = duenoService.obtenerPorId(idDueno);
        if (dueno == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(dueno);
    }

    @PutMapping("/perfil/{id}/editar")
    public ResponseEntity<Dueno> editarDueno(@PathVariable("id") ObjectId idDueno, @RequestBody Dueno duenoActualizado, HttpSession session) {

        ObjectId idEnSesion = (ObjectId) session.getAttribute("idDueño");

        if (idEnSesion == null) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!idEnSesion.equals(idDueno)) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


        Dueno dueno = duenoService.actualizarDueno(idDueno ,duenoActualizado);
        if (dueno == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(dueno);
    }

    @GetMapping("/perfil/{id}/mascotas")
    public ResponseEntity<List<Mascota>> obtenerMacotasDueno(@PathVariable("id")ObjectId idDueno, HttpSession session){

        ObjectId idEnSesion = (ObjectId) session.getAttribute("idDueño");

        if (idEnSesion == null) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!idEnSesion.equals(idDueno)) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


        Dueno dueno = duenoService.obtenerPorId(idDueno);

        if (dueno == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<Mascota> mascotas = dueno.getMascotas();

        return ResponseEntity.status(HttpStatus.OK).body(mascotas);

    }

    @PostMapping("/perfil/{id}/mascotas/agregar")
    public ResponseEntity<Mascota> crearMacotasDueno(@PathVariable("id")ObjectId idDueno, @RequestBody Mascota mascota , HttpSession session, @RequestBody MultipartFile file){

        ObjectId idEnSesion = (ObjectId) session.getAttribute("idDueño");

        if (idEnSesion == null) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!idEnSesion.equals(idDueno)) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


        Dueno dueno = duenoService.obtenerPorId(idDueno);

        if (dueno == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        dueno.getMascotas().add(mascota);
        duenoService.guardarMascota(idDueno, mascota, file);

        Mascota mascotaRegistrada = duenoService.obtenerPorId(idDueno).getMascotas().getLast();


        return ResponseEntity.status(HttpStatus.OK).body(mascotaRegistrada);

    }

    @GetMapping("/perfil/{id}/citas")
    public ResponseEntity<List<Reserva>> obtenerM(@PathVariable("id")ObjectId idDueno, HttpSession session){

        ObjectId idEnSesion = (ObjectId) session.getAttribute("idDueño");

        if (idEnSesion == null) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!idEnSesion.equals(idDueno)) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


        Dueno dueno = duenoService.obtenerPorId(idDueno);

        if (dueno == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        reservaService.obtene

        return ResponseEntity.status(HttpStatus.OK).body(mascotas);

    }



}
