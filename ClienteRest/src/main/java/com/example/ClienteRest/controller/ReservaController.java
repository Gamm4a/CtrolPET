package com.example.ClienteRest.controller;

import com.example.ClienteRest.Mapper.Mappers;
import com.example.ClienteRest.dtos.ReservaDTO;
import com.example.ClienteRest.dtos.ReservaSinLoginDTO;
import com.example.ClienteRest.services.JwtService;
import com.example.ctrolpet.exception.ResourceNotFoundException;
import com.example.ctrolpet.exception.UnauthorizedActionException;
import com.example.ctrolpet.model.Dueno;
import com.example.ctrolpet.model.Empleado;
import com.example.ctrolpet.model.Enum.EstadoReserva;
import com.example.ctrolpet.model.Mascota;
import com.example.ctrolpet.model.Reserva;
import com.example.ctrolpet.model.Servicio;
import com.example.ctrolpet.service.DuenoService;
import com.example.ctrolpet.service.EmpleadoService;
import com.example.ctrolpet.service.ReservaService;
import com.example.ctrolpet.service.ServicioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private DuenoService duenoService;

    @Autowired
    private ServicioService servicioService;

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping("/horarios")
    public ResponseEntity<Map<LocalTime, String>> obtenerHorariosDisponibles(
            @RequestParam String idSucursal,
            @RequestParam String idServicio,
            @RequestParam String fecha) {

        Servicio servicio = servicioService.obtenerPorId(new ObjectId(idServicio));
        ObjectId sucursalId = new ObjectId(idSucursal);
        LocalDate fechaConsulta = LocalDate.parse(fecha);
        String diaSemana = diaEnEspanol(fechaConsulta);

        Map<LocalTime, String> mapaHorarios = new TreeMap<>();

        for (Empleado emp : empleadoService.empleadoDisponible(servicio.getCategoria().name(), diaSemana)) {
            if (emp.getSucursal() == null || !emp.getSucursal().equals(sucursalId)) {
                continue;
            }

            List<Reserva> reservasDelDia = reservaService.obtenerPorEmpleadoYFecha(emp.getIdEmpleado(), fechaConsulta);
            List<LocalTime> rangos = servicioService.rangoCitas(
                    emp.getHorarios().getHoraEntrada(),
                    emp.getHorarios().getHoraSalida(),
                    servicio.getDuracion());

            for (LocalTime hora : rangos) {
                if (!reservaService.horarioOcupado(hora, servicio.getDuracion(), reservasDelDia)) {
                    mapaHorarios.put(hora,
                            emp.getIdEmpleado().toString() + "|" + emp.getNombre() + " " + emp.getApellidoPaterno());
                }
            }
        }

        return ResponseEntity.ok(mapaHorarios);
    }

    // Crear reserva sin login
    @PostMapping
    public ResponseEntity<ReservaDTO> crearReservaSinLogin(@Valid @RequestBody ReservaSinLoginDTO datos) {

        // Mascota nueva (aún no tiene dueño registrado)
        Mascota mascota = new Mascota();
        mascota.setIdMascota(new ObjectId());
        mascota.setNombre(datos.getNombreMascota());
        mascota.setEspecie(datos.getEspecieMascota());
        mascota.setRaza(datos.getRazaMascota());

        // solo nombre y teléfono, sin correo/contraseña
        Dueno dueno = new Dueno();
        dueno.setNombre(datos.getNombre());
        dueno.setTelefono(datos.getTelefono());
        dueno.setMascotas(new ArrayList<>(List.of(mascota)));
        Dueno duenoGuardado = duenoService.guardar(dueno);

        // Buscar un veterinario disponible para ese servicio
        Servicio servicio = servicioService.obtenerPorId(new ObjectId(datos.getIdServicio()));
        ObjectId idSucursal = new ObjectId(datos.getIdSucursal());
        String diaSemana = diaEnEspanol(datos.getFecha());

        Empleado empleadoAsignado = null;
        for (Empleado emp : empleadoService.empleadoDisponible(servicio.getCategoria().name(), diaSemana)) {
            if (emp.getSucursal() != null && emp.getSucursal().equals(idSucursal)) {
                List<Reserva> reservasDelDia = reservaService.obtenerPorEmpleadoYFecha(emp.getIdEmpleado(),
                        datos.getFecha());
                if (!reservaService.horarioOcupado(datos.getHora(), servicio.getDuracion(), reservasDelDia)) {
                    empleadoAsignado = emp;
                    break;
                }
            }
        }

        if (empleadoAsignado == null) {
            throw new IllegalArgumentException("No hay veterinarios disponibles en ese horario");
        }

        // Crear y guardar la reserva
        Reserva reserva = new Reserva();
        reserva.setIdEmpleado(empleadoAsignado.getIdEmpleado());
        reserva.setFecha(LocalDateTime.of(datos.getFecha(), datos.getHora()));
        reserva.setEstado(EstadoReserva.PENDIENTE);
        reserva.setIdSucursal(idSucursal);
        reserva.setDueno(duenoGuardado.getIdDueno());
        reserva.setMascota(mascota.getIdMascota());
        reserva.setServicios(servicio);

        Reserva guardada = reservaService.guardar(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(Mappers.toDTO(guardada));
    }

    // Crear reserva con login
    @PostMapping("/dueno/{id}")
    public ResponseEntity<ReservaDTO> crearReservaConLogin(@PathVariable("id") ObjectId idDueno,
            @Valid @RequestBody ReservaDTO reservaDTO, HttpServletRequest request) {

        String emailEnToken = (String) request.getAttribute("email");
        Dueno dueno = duenoService.obtenerPorId(idDueno);

        if (dueno == null) {
            throw new ResourceNotFoundException("El dueño no existe");
        }

        if (!dueno.getCorreo().equals(emailEnToken)) {
            throw new UnauthorizedActionException("No tienes acceso a esta cuenta");
        }

        Reserva guardada = reservaService.guardar(Mappers.toEntity(reservaDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(Mappers.toDTO(guardada));
    }

    private String diaEnEspanol(LocalDate fecha) {
        String dia = fecha.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "MX")).toUpperCase();
        return java.text.Normalizer.normalize(dia, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}