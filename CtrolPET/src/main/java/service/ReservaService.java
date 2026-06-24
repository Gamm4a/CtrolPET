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

    public Reserva guardar(Reserva reserva){

        reserva.setEstado(EstadoReserva.PENDIENTE);

        if (LocalDateTime.now().isBefore(reserva.getFecha())
                && reserva.getMascota() != null
                && reserva.getServicio() != null
                && reserva.getId_Empleado() != null){

            return reservaRepository.save(reserva);

        }

        return null;

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

    public Reserva actualizarCompleto(ObjectId id, Reserva reservaActualizado){

        if (LocalDateTime.now().isBefore(reservaActualizado.getFecha())
                && reservaActualizado.getMascota() != null
                && reservaActualizado.getServicio() != null
                && reservaActualizado.getId_Empleado() != null
                && reservaActualizado.getEstado() != null) {

            return reservaRepository.findById(id).map(reserva -> {
                reserva.setId_Empleado(reservaActualizado.getId_Empleado());
                reserva.setEstado(reservaActualizado.getEstado());
                reserva.setFecha(reservaActualizado.getFecha());
                reserva.setMascota(reservaActualizado.getMascota());
                reserva.setServicio(reservaActualizado.getServicio());
                return reservaRepository.save(reserva);
            }).orElse(null);

        }

        return null;

    }

    public Reserva actualizarParcial(ObjectId id, Reserva reservaParcial) {

            return reservaRepository.findById(id).map(reserva -> {
                if (reservaParcial.getId_Empleado() != null) reserva.setId_Empleado(reservaParcial.getId_Empleado());
                if (reservaParcial.getEstado() != null) reserva.setEstado(reservaParcial.getEstado());
                if (reservaParcial.getFecha() != null && LocalDateTime.now().isBefore(reservaParcial.getFecha())) reserva.setFecha(reservaParcial.getFecha());
                if (reservaParcial.getMascota() != null) reserva.setMascota(reservaParcial.getMascota());
                if (reservaParcial.getServicio() != null) reserva.setServicio(reservaParcial.getServicio());

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
