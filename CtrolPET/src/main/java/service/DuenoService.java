package service;


import model.Dueno;
import model.Mascota;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import repository.DuenoRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class DuenoService {

    @Autowired
    private DuenoRepository duenoRepository;

    public Dueno guardar(Dueno dueno){

        // Validar nombre
        if (dueno.getNombre() == null || dueno.getNombre().trim().isEmpty()) {
            return null;
        }

        // Validar apellidos
        if (dueno.getAp_paterno() == null || dueno.getAp_paterno().trim().isEmpty()) {
            return null;
        }
        if (dueno.getAp_materno() == null || dueno.getAp_materno().trim().isEmpty()) {
        return null;
        }

        // Validar fecha de nacimiento
        if (dueno.getFch_nacimiento() == null) {
            return null;
        }
        if (dueno.getFch_nacimiento().isAfter(Instant.now())) {
            return null;
        }

        // Validar edad mínima (18 años)
        LocalDate nacimiento = dueno.getFch_nacimiento()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate hoy = LocalDate.now();
        Period edad = Period.between(nacimiento, hoy);
        if (edad.getYears() < 18) {
            return null;
        }

        // Validar teléfono (ejemplo: 10 dígitos)
        if (dueno.getTelefono() == null || !dueno.getTelefono().matches("\\d{10}")) {
            return null;
        }

        // Validar dirección
        if (dueno.getDireccion() == null) {
            return null;
        }

        // Validar lista de mascotas
        if (dueno.getMascotas() == null) {
            dueno.setMascotas(new ArrayList<>());
        }

        return duenoRepository.save(dueno);
    }

    public List<Dueno> obtenerTodos(){
        Pageable pageable = PageRequest.of(0, 20);
        return duenoRepository.findAll(pageable).getContent();
    }

    public Dueno obtenerPorId(String id){
        if (id == null || id.trim().isEmpty()){
            return null;
        }

        ObjectId idObj = new ObjectId(id);
        if (idObj == null)
            return null;

        return duenoRepository.findById(idObj).orElse(null);
    }

    public Dueno obtenerPorId(ObjectId id){
        return duenoRepository.findById(id).orElse(null);
    }

    public void eliminar(ObjectId id){
        duenoRepository.deleteById(id);
    }

    public Dueno actualizarCompleto(String id, Dueno duenoActualizado) {
        ObjectId idObj = new ObjectId(id);
        return duenoRepository.findById(idObj).map(dueno -> {
            dueno.setNombre(duenoActualizado.getNombre());
            dueno.setDireccion(duenoActualizado.getDireccion());
            dueno.setAp_materno(duenoActualizado.getAp_materno());
            dueno.setAp_paterno(duenoActualizado.getAp_paterno());
            dueno.setMascotas(duenoActualizado.getMascotas());
            dueno.setTelefono(dueno.getTelefono());
            dueno.setFch_nacimiento(dueno.getFch_nacimiento());
            return duenoRepository.save(dueno);
        }).orElse(null);
    }

    public Dueno actualizarCompleto(ObjectId id, Dueno duenoActualizado) {
        return duenoRepository.findById(id).map(dueno -> {
            dueno.setNombre(duenoActualizado.getNombre());
            dueno.setDireccion(duenoActualizado.getDireccion());
            dueno.setAp_materno(duenoActualizado.getAp_materno());
            dueno.setAp_paterno(duenoActualizado.getAp_paterno());
            dueno.setMascotas(duenoActualizado.getMascotas());
            dueno.setTelefono(dueno.getTelefono());
            dueno.setFch_nacimiento(dueno.getFch_nacimiento());
            return duenoRepository.save(dueno);
        }).orElse(null);
    }

    public Dueno actualizarParcial(String id, Dueno duenoParcial) {
        ObjectId idObj = new ObjectId();
        return duenoRepository.findById(idObj).map(dueno -> {
            if (duenoParcial.getNombre()!=null) dueno.setNombre(duenoParcial.getNombre());
            if (duenoParcial.getDireccion()!=null) dueno.setDireccion(duenoParcial.getDireccion());
            if (duenoParcial.getAp_materno()!=null) dueno.setAp_materno(duenoParcial.getAp_materno());
            if (duenoParcial.getAp_paterno()!=null) dueno.setAp_paterno(duenoParcial.getAp_paterno());
            if (duenoParcial.getMascotas()!=null || duenoParcial.getMascotas().isEmpty()) dueno.setMascotas(duenoParcial.getMascotas());
            if (duenoParcial.getTelefono()!=null) dueno.setTelefono(duenoParcial.getTelefono());
            if (duenoParcial.getFch_nacimiento()!=null) dueno.setFch_nacimiento(duenoParcial.getFch_nacimiento());
            return duenoRepository.save(dueno);
        }).orElse(null);
    }

    public Dueno actualizarParcial(ObjectId id, Dueno duenoParcial) {
        return duenoRepository.findById(id).map(dueno -> {
            if (duenoParcial.getNombre()!=null) dueno.setNombre(duenoParcial.getNombre());
            if (duenoParcial.getDireccion()!=null) dueno.setDireccion(duenoParcial.getDireccion());
            if (duenoParcial.getAp_materno()!=null) dueno.setAp_materno(duenoParcial.getAp_materno());
            if (duenoParcial.getAp_paterno()!=null) dueno.setAp_paterno(duenoParcial.getAp_paterno());
            if (duenoParcial.getMascotas()!=null || duenoParcial.getMascotas().isEmpty()) dueno.setMascotas(duenoParcial.getMascotas());
            if (duenoParcial.getTelefono()!=null) dueno.setTelefono(duenoParcial.getTelefono());
            if (duenoParcial.getFch_nacimiento()!=null) dueno.setFch_nacimiento(duenoParcial.getFch_nacimiento());
            return duenoRepository.save(dueno);
        }).orElse(null);
    }
    public Dueno anadirMascota(String id,Mascota mascota){
        ObjectId idObj = new ObjectId(id);
        Dueno dueno = duenoRepository.findById(idObj).orElse(null);
        dueno.getMascotas().add(mascota);
        return actualizarParcial(id, dueno);
    }

    public Dueno anadirMascota(ObjectId id,Mascota mascota){
        Dueno dueno = duenoRepository.findById(id).orElse(null);
        dueno.getMascotas().add(mascota);
        return actualizarParcial(id, dueno);
    }
    public Dueno anadirMascota(Dueno dueno ,Mascota mascota){
        Dueno duenoActualizado =dueno;
        dueno.getMascotas().add(mascota);
        return actualizarParcial(dueno.getId_dueño(), dueno);

    }

}
