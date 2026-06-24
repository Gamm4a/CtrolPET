package com.example.ctrolpet.service;

import jakarta.servlet.http.HttpSession;
import com.example.ctrolpet.model.Empleado;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.ctrolpet.repository.EmpleadosRepository;

import java.util.Optional;

@Service
public class EmpleadoService {
    @Autowired
    private EmpleadosRepository empleadosRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HttpSession httpSession;

    public Empleado autenticar(String correo, String contrasenia){
        Optional<Empleado> empleadoOptional = empleadosRepository.findByCorreo(correo);

        if(empleadoOptional.isPresent()){
            Empleado empleado = empleadoOptional.get();
            if(passwordEncoder.matches(contrasenia, empleado.getContrasenia())){
                return empleado;
            }
        }

        return null;
    }

    public Empleado obtenerEmpleadoLogueado(){
        ObjectId empeladoId = (ObjectId) httpSession.getAttribute("id_Empleado");
        if (empeladoId != null){
            Optional <Empleado> empleadoOptional = empleadosRepository.findById(empeladoId);
            return empleadoOptional.get();
        }

        return null;
    }
}
