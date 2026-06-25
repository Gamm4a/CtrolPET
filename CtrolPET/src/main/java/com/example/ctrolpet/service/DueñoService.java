package com.example.ctrolpet.service;

import com.example.ctrolpet.model.Dueno;
import jakarta.servlet.http.HttpSession;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.ctrolpet.repository.DuenoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DueñoService {
    @Autowired
    private DuenoRepository duenoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HttpSession httpSession;

    public Dueno autenticar(String correo, String contrasenia){
        Optional<Dueno> dueñoOptional = duenoRepository.findByCorreo(correo);

        if(dueñoOptional.isPresent()){
            Dueno dueno = dueñoOptional.get();
            if(passwordEncoder.matches(contrasenia, dueno.getContrasenia())){
                return dueno;
            }
        }

        return null;
    }

    public Dueno obtenerDueñoLogueado(){
        ObjectId dueñoId = (ObjectId) httpSession.getAttribute("id_dueño");
        if (dueñoId != null){
            Optional <Dueno> dueñoOptional = duenoRepository.findById(dueñoId);
            return dueñoOptional.get();
        }

        return null;
    }

    public List<Dueno> obtenerTodos(){

        Pageable pageable = (Pageable) PageRequest.of(0, 20);

        return duenoRepository.findAll(pageable).getContent();

    }

    public Dueno obtenerPorId(ObjectId id){

        return duenoRepository.findById(id).orElse(null);

    }

    public Dueno guardar(Dueno dueno){

        return duenoRepository.save(dueno);

    }



}
