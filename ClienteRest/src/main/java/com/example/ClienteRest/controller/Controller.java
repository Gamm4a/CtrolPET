package com.example.ClienteRest.controller;
import com.example.ClienteRest.Mapper.Mappers;
import com.example.ClienteRest.dtos.*;
import com.example.ClienteRest.services.JwtService;
import com.example.ctrolpet.model.Dueno;
import com.example.ctrolpet.model.Reserva;
import com.example.ctrolpet.service.DuenoService;
import com.example.ctrolpet.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    private DuenoService duenoService;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private JwtService jwtService;


    @PostMapping("/registro")
    public ResponseEntity<DuenoDTO> registrarDueno(@Valid @RequestBody RegistroDTO registroDTO) {

        Dueno dueno = Mappers.toEntity(registroDTO.getDuenoDTO());
        Dueno duenoRegistrado = duenoService.guardar(dueno);

        if (registroDTO.getReservaDTO() != null && duenoRegistrado.getMascotas() != null && !duenoRegistrado.getMascotas().isEmpty()) {
            Reserva reserva = Mappers.toEntity(registroDTO.getReservaDTO());
            reserva.setMascota(duenoRegistrado.getMascotas().getFirst().getIdMascota());
            reserva.setDueno(duenoRegistrado.getIdDueno());
            reservaService.guardar(reserva);
        }


        return ResponseEntity.status(HttpStatus.CREATED).body(Mappers.toDTO(duenoRegistrado));
    };

    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesionDueno(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        Dueno duenoRegistrado = duenoService.autenticar(loginRequestDTO.getCorreo(), loginRequestDTO.getContrasenia());

        String token = jwtService.generateToken(duenoRegistrado.getCorreo());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
