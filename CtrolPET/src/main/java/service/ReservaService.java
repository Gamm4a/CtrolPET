package service;


import model.Enum.EstadoReserva;
import model.Mascota;
import model.Reserva;
import model.Servicio;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import repository.ReservaRepository;


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

        if (reserva.getServicio() == null) {
            throw new IllegalArgumentException("Debe seleccionar un servicio para la reserva.");
        }

        if (reserva.getId_Empleado() == null) {
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
        if (reservaActualizado.getServicio() == null) {
            throw new IllegalArgumentException("La reserva debe tener un servicio.");
        }
        if (reservaActualizado.getId_Empleado() == null) {
            throw new IllegalArgumentException("La reserva debe tener un empleado asignado.");
        }
        if (reservaActualizado.getEstado() == null) {
            throw new IllegalArgumentException("El estado de la reserva no puede ser nulo.");
        }
        if (reservaActualizado.getFecha() == null || !LocalDateTime.now().isBefore(reservaActualizado.getFecha())) {
            throw new IllegalArgumentException("La nueva fecha debe ser un momento en el futuro.");
        }


        return reservaRepository.findById(id).map(reservaExistente -> {
            reservaExistente.setId_Empleado(reservaActualizado.getId_Empleado());
            reservaExistente.setEstado(reservaActualizado.getEstado());
            reservaExistente.setFecha(reservaActualizado.getFecha());
            reservaExistente.setMascota(reservaActualizado.getMascota());
            reservaExistente.setServicio(reservaActualizado.getServicio());
            return reservaRepository.save(reservaExistente);
        }).orElse(null);

    }

    public Reserva actualizarParcial(ObjectId id, Reserva reservaParcial) {
        return reservaRepository.findById(id).map(reserva -> {

            if (reservaParcial.getId_Empleado() != null) {
                reserva.setId_Empleado(reservaParcial.getId_Empleado());
            }
            if (reservaParcial.getEstado() != null) {
                reserva.setEstado(reservaParcial.getEstado());
            }
            if (reservaParcial.getMascota() != null) {
                reserva.setMascota(reservaParcial.getMascota());
            }
            if (reservaParcial.getServicio() != null) {
                reserva.setServicio(reservaParcial.getServicio());
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

    public List<Reserva> obtenerPorEmpleado(ObjectId id_Empleado){

        Pageable pageable = (Pageable) PageRequest.of(0, 20);

        return reservaRepository.findById_Empleado(id_Empleado,pageable).getContent();

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

        return reservaRepository.findByServicio(servicio,pageable).getContent();

    }


}
