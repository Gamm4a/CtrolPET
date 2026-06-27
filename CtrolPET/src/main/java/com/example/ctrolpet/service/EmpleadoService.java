package com.example.ctrolpet.service;

import jakarta.servlet.http.HttpSession;
import com.example.ctrolpet.model.Empleado;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.ctrolpet.repository.EmpleadosRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoService {
    @Autowired
    private EmpleadosRepository empleadosRepository;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Autowired
    private HttpSession httpSession;

    public Empleado autenticar(String correo, String contrasenia){
        Optional<Empleado> empleadoOptional = empleadosRepository.findByCorreo(correo);

        if(empleadoOptional.isPresent()){
            Empleado empleado = empleadoOptional.get();
            if(contrasenia.matches(empleado.getContrasenia())){
                return empleado;
            }
//            if(passwordEncoder.matches(contrasenia, empleado.getContrasenia())){
//                return empleado;
//            }
        }

        return null;
    }

    public Empleado obtenerEmpleadoLogueado(){
        ObjectId empeladoId = (ObjectId) httpSession.getAttribute("idEmpleado");
        if (empeladoId != null){
            Optional <Empleado> empleadoOptional = empleadosRepository.findById(empeladoId);
            return empleadoOptional.get();
        }

        return null;
    }


    public Empleado guardar(Empleado empleado) {

        if (empleado.getNombre() == null || empleado.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del empleado es obligatorio.");
        }
        if (empleado.getApellidoPaterno() == null || empleado.getApellidoPaterno().isBlank() ||
                empleado.getApellidoMaterno() == null || empleado.getApellidoMaterno().isBlank()) {
            throw new IllegalArgumentException("Ambos apellidos son obligatorios.");
        }
        if (empleado.getTelefono() == null || !empleado.getTelefono().matches("^\\d{10}$")) {
            throw new IllegalArgumentException("El teléfono debe contener exactamente 10 dígitos.");
        }
        if (empleado.getPuesto() == null) {
            throw new IllegalArgumentException("El puesto es obligatorio.");
        }
        if (empleado.getHorarios() == null) {
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
        if (empleadoActualizado.getApellidoPaterno() == null || empleadoActualizado.getApellidoPaterno().isBlank() ||
                empleadoActualizado.getApellidoMaterno() == null || empleadoActualizado.getApellidoMaterno().isBlank()) {
            throw new IllegalArgumentException("Ambos apellidos son obligatorios.");
        }
        if (empleadoActualizado.getTelefono() == null || !empleadoActualizado.getTelefono().matches("^\\d{10}$")) {
            throw new IllegalArgumentException("El teléfono debe contener exactamente 10 dígitos.");
        }
        if (empleadoActualizado.getPuesto() == null) {
            throw new IllegalArgumentException("El puesto es obligatorio.");
        }
        if (empleadoActualizado.getHorarios() == null) {
            throw new IllegalArgumentException("El empleado debe tener al menos un horario asignado.");
        }


        return empleadosRepository.findById(id).map(empleado -> {
            empleado.setNombre(empleadoActualizado.getNombre());
            empleado.setApellidoPaterno(empleadoActualizado.getApellidoPaterno());
            empleado.setApellidoMaterno(empleadoActualizado.getApellidoMaterno());
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

            if (empleadoParcial.getApellidoPaterno() != null && !empleadoParcial.getApellidoPaterno().isBlank()) {
                empleado.setApellidoPaterno(empleadoParcial.getApellidoPaterno());
            }

            if (empleadoParcial.getApellidoMaterno() != null && !empleadoParcial.getApellidoMaterno().isBlank()) {
                empleado.setApellidoMaterno(empleadoParcial.getApellidoMaterno());
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
                empleado.setHorarios(empleadoParcial.getHorarios());
            }

            return empleadosRepository.save(empleado);
        }).orElse(null);
    }

}


