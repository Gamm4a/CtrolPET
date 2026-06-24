package service;

import model.Empleado;
import model.Reserva;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import repository.EmpleadosRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadosRepository empleadosRepository;

    public Empleado guardar(Empleado empleado) {

        if (empleado.getNombre() == null || empleado.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del empleado es obligatorio.");
        }
        if (empleado.getAp_paterno() == null || empleado.getAp_paterno().isBlank() ||
                empleado.getAp_materno() == null || empleado.getAp_materno().isBlank()) {
            throw new IllegalArgumentException("Ambos apellidos son obligatorios.");
        }
        if (empleado.getTelefono() == null || !empleado.getTelefono().matches("^\\d{10}$")) {
            throw new IllegalArgumentException("El teléfono debe contener exactamente 10 dígitos.");
        }
        if (empleado.getPuesto() == null) {
            throw new IllegalArgumentException("El puesto es obligatorio.");
        }
        if (empleado.getHorarios() == null || empleado.getHorarios().isEmpty()) {
            throw new IllegalArgumentException("El empleado debe tener al menos un horario asignado.");
        }


        return empleadosRepository.save(empleado);
    }

    public List<Empleado> obtenerTodos(){

        Pageable pageable = (Pageable) PageRequest.of(0, 20);

        return empleadosRepository.findAll(pageable).getContent();

    }

    public Empleado obtenerPorId(ObjectId id){

        return empleadosRepository.findById(id).orElse(null);

    }

    public void eliminar(ObjectId id){

        empleadosRepository.deleteById(id);

    }

    public Empleado actualizarCompleto(ObjectId id, Empleado empleadoActualizado) {

        if (empleadoActualizado.getNombre() == null || empleadoActualizado.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del empleado es obligatorio.");
        }
        if (empleadoActualizado.getAp_paterno() == null || empleadoActualizado.getAp_paterno().isBlank() ||
                empleadoActualizado.getAp_materno() == null || empleadoActualizado.getAp_materno().isBlank()) {
            throw new IllegalArgumentException("Ambos apellidos son obligatorios.");
        }
        if (empleadoActualizado.getTelefono() == null || !empleadoActualizado.getTelefono().matches("^\\d{10}$")) {
            throw new IllegalArgumentException("El teléfono debe contener exactamente 10 dígitos.");
        }
        if (empleadoActualizado.getPuesto() == null) {
            throw new IllegalArgumentException("El puesto es obligatorio.");
        }
        if (empleadoActualizado.getHorarios() == null || empleadoActualizado.getHorarios().isEmpty()) {
            throw new IllegalArgumentException("El empleado debe tener al menos un horario asignado.");
        }


        return empleadosRepository.findById(id).map(empleado -> {
            empleado.setNombre(empleadoActualizado.getNombre());
            empleado.setAp_paterno(empleadoActualizado.getAp_paterno());
            empleado.setAp_materno(empleadoActualizado.getAp_materno());
            empleado.setTelefono(empleadoActualizado.getTelefono());
            empleado.setPuesto(empleadoActualizado.getPuesto());
            empleado.setHorarios(empleadoActualizado.getHorarios());

            return empleadosRepository.save(empleado);
        }).orElse(null);
    }

    public Empleado actualizarParcial(ObjectId id, Empleado empleadoParcial) {

        return empleadosRepository.findById(id).map(empleado -> {

            if (empleadoParcial.getNombre() != null && !empleadoParcial.getNombre().isBlank()) {
                empleado.setNombre(empleadoParcial.getNombre());
            }

            if (empleadoParcial.getAp_paterno() != null && !empleadoParcial.getAp_paterno().isBlank()) {
                empleado.setAp_paterno(empleadoParcial.getAp_paterno());
            }

            if (empleadoParcial.getAp_materno() != null && !empleadoParcial.getAp_materno().isBlank()) {
                empleado.setAp_materno(empleadoParcial.getAp_materno());
            }


            if (empleadoParcial.getTelefono() != null) {
                if (!empleadoParcial.getTelefono().matches("^\\d{10}$")) {
                    throw new IllegalArgumentException("El teléfono modificado debe contener exactamente 10 dígitos.");
                }
                empleado.setTelefono(empleadoParcial.getTelefono());
            }

            if (empleadoParcial.getPuesto() != null) {
                empleado.setPuesto(empleadoParcial.getPuesto());
            }


            if (empleadoParcial.getHorarios() != null) {
                if (empleadoParcial.getHorarios().isEmpty()) {
                    throw new IllegalArgumentException("No se puede dejar al empleado sin horarios.");
                }
                empleado.setHorarios(empleadoParcial.getHorarios());
            }

            return empleadosRepository.save(empleado);
        }).orElse(null);
    }

}
