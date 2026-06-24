package com.example.ctrolpet.service;

import jakarta.servlet.http.HttpSession;
import com.example.ctrolpet.model.Dueño;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.ctrolpet.repository.DueñoRepository;

import java.util.Optional;

@Service
public class DueñoService {
    @Autowired
    private DueñoRepository dueñoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HttpSession httpSession;

    public Dueño autenticar(String correo, String contrasenia){
        Optional<Dueño> dueñoOptional = dueñoRepository.findByCorreo(correo);

        if(dueñoOptional.isPresent()){
            Dueño dueño = dueñoOptional.get();
            if(passwordEncoder.matches(contrasenia, dueño.getContrasenia())){
                return dueño;
            }
        }

        return null;
    }

    public Dueño obtenerDueñoLogueado(){
        ObjectId dueñoId = (ObjectId) httpSession.getAttribute("id_dueño");
        if (dueñoId != null){
            Optional <Dueño> dueñoOptional = dueñoRepository.findById(dueñoId);
            return dueñoOptional.get();
        }

        return null;
    }
}
