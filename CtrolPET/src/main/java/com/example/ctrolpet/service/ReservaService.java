package com.example.ctrolpet.service;



import com.example.ctrolpet.model.Enum.EstadoReserva;
import com.example.ctrolpet.model.Dueno;
import com.example.ctrolpet.model.Empleado;
import com.example.ctrolpet.model.Mascota;
import com.example.ctrolpet.model.Reserva;
import com.example.ctrolpet.model.Servicio;
import com.example.ctrolpet.repository.ReservaRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    public Reserva guardar(Reserva reserva) {


        reserva.setEstado(EstadoReserva.PENDIENTE);

        if (reserva.getMascota() == null) {
            throw new IllegalArgumentException("La reserva debe estar asociada a una mascota válida.");
        }

        if (reserva.getServicios() == null) {
            throw new IllegalArgumentException("Debe seleccionar un servicio para la reserva.");
        }

        if (reserva.getIdEmpleado() == null) {
            throw new IllegalArgumentException("Debe asignar un empleado/especialista a la reserva.");
        }

        if (reserva.getFecha() == null || !LocalDateTime.now().isBefore(reserva.getFecha())) {
            throw new IllegalArgumentException("La fecha de la reserva debe ser un momento en el futuro.");
        }


        return reservaRepository.save(reserva);
    }

    public List<Reserva> obtenerTodos(){

        Pageable pageable = (Pageable) PageRequest.of(0, 20);

        return reservaRepository.findAll(pageable).getContent();

    }

    public Reserva obtenerPorId(ObjectId id){

        return reservaRepository.findById(id).orElse(null);

    }

    public void eliminar(ObjectId id){

        reservaRepository.deleteById(id);

    }

    public Reserva actualizarCompleto(ObjectId id, Reserva reservaActualizado) {

        if (reservaActualizado.getMascota() == null) {
            throw new IllegalArgumentException("La reserva debe tener una mascota.");
        }
        if (reservaActualizado.getServicios() == null) {
            throw new IllegalArgumentException("La reserva debe tener un servicio.");
        }
        if (reservaActualizado.getIdEmpleado() == null) {
            throw new IllegalArgumentException("La reserva debe tener un empleado asignado.");
        }
        if (reservaActualizado.getEstado() == null) {
            throw new IllegalArgumentException("El estado de la reserva no puede ser nulo.");
        }
        if (reservaActualizado.getFecha() == null || !LocalDateTime.now().isBefore(reservaActualizado.getFecha())) {
            throw new IllegalArgumentException("La nueva fecha debe ser un momento en el futuro.");
        }


        return reservaRepository.findById(id).map(reservaExistente -> {
            reservaExistente.setIdEmpleado(reservaActualizado.getIdEmpleado());
            reservaExistente.setEstado(reservaActualizado.getEstado());
            reservaExistente.setFecha(reservaActualizado.getFecha());
            reservaExistente.setMascota(reservaActualizado.getMascota());
            reservaExistente.setServicios(reservaActualizado.getServicios());
            return reservaRepository.save(reservaExistente);
        }).orElse(null);

    }

    public Reserva actualizarParcial(ObjectId id, Reserva reservaParcial) {
        return reservaRepository.findById(id).map(reserva -> {

            if (reservaParcial.getIdEmpleado() != null) {
                reserva.setIdEmpleado(reservaParcial.getIdEmpleado());
            }
            if (reservaParcial.getEstado() != null) {
                reserva.setEstado(reservaParcial.getEstado());
            }
            if (reservaParcial.getMascota() != null) {
                reserva.setMascota(reservaParcial.getMascota());
            }
            if (reservaParcial.getServicios() != null) {
                reserva.setServicios(reservaParcial.getServicios());
            }


            if (reservaParcial.getFecha() != null) {
                if (!LocalDateTime.now().isBefore(reservaParcial.getFecha())) {
                    throw new IllegalArgumentException("La nueva fecha modificada debe ser en el futuro.");
                }
                reserva.setFecha(reservaParcial.getFecha());
            }

            return reservaRepository.save(reserva);
        }).orElse(null);
    }

    public List<Reserva> obtenerPorEmpleado(ObjectId idEmpleado){
        Pageable pageable = (Pageable) PageRequest.of(0, 20);
        return reservaRepository.findByIdEmpleado(idEmpleado,pageable).getContent();

    }

    public List<Reserva> obtenerPorEstado(EstadoReserva estadoReserva){
        Pageable pageable = (Pageable) PageRequest.of(0, 20);
        return reservaRepository.findByEstado(estadoReserva,pageable).getContent();

    }

    public List<Reserva> obtenerPorMascota(Mascota mascota){
        Pageable pageable = (Pageable) PageRequest.of(0, 20);
        return reservaRepository.findByMascota(mascota,pageable).getContent();

    }

    public List<Reserva> obtenerPorServicio(Servicio servicio){
        Pageable pageable = (Pageable) PageRequest.of(0, 20);
        return reservaRepository.findByServicios(servicio,pageable).getContent();

    }
    

    public List<Reserva> buscar(String texto, DuenoService duenoService, EmpleadoService empleadoService){
        List<Reserva> todas = obtenerTodos();
        if(texto == null || texto.isBlank()){
            return todas;
        }

        String textoBuscado = texto.toLowerCase();

         return todas.stream().filter(r -> {
            Dueno dueno = duenoService.obtenerPorId(r.getDueno());
            if(dueno != null && dueno.getNombre().toLowerCase().contains(textoBuscado)) return true;
            
            if(dueno != null){
                Mascota mascota = duenoService.getMascotaById(r.getMascota(),r.getDueno());
                if(mascota != null && mascota.getNombre().toLowerCase().contains(textoBuscado)) return true;

            }
           Empleado empleado = empleadoService.obtenerPorId(r.getIdEmpleado());
            if(empleado != null && empleado.getNombre().toLowerCase().contains(textoBuscado)) return true;

            Servicio servicio = r.getServicios();
            if(servicio != null){
                if(servicio.getTipo() != null && servicio.getTipo().toLowerCase().contains(textoBuscado)) return true;
                if(servicio.getDescripcion() != null && servicio.getDescripcion().toLowerCase().contains(textoBuscado)) return true;
            }

            return false;

        }).collect(java.util.stream.Collectors.toList());
    }


}
