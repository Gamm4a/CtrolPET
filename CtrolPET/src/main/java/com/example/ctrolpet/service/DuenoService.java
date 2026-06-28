package com.example.ctrolpet.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.ctrolpet.model.Dueno;
import com.example.ctrolpet.model.Mascota;
import jakarta.servlet.http.HttpSession;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.ctrolpet.repository.DuenoRepository;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DuenoService {
    @Autowired
    private DuenoRepository duenoRepository;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private HttpSession httpSession;

    public Dueno autenticar(String correo, String contrasenia){
        Optional<Dueno> dueñoOptional = duenoRepository.findByCorreo(correo);

        if(dueñoOptional.isPresent()){
            Dueno dueno = dueñoOptional.get();
            if(contrasenia.matches(dueno.getContrasenia())){
                return dueno;
            }
//            if(passwordEncoder.matches(contrasenia, dueno.getContrasenia())){
//                return dueno;
//            }
        }

        return null;
    }

    public Dueno obtenerDuenoLogueado(){
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

    public Mascota getMascotaById(ObjectId idMascota, ObjectId idDueno){
        Optional <Dueno> duenoOptional = duenoRepository.findById(idDueno);
        List<Mascota> mascotas = duenoOptional.get().getMascotas();
        for (Mascota m:mascotas){
            if (m.getIdMascota().equals(idMascota)){
                return m;
            }
        }
        return null;
    }
    public void guardarFotoMascota(ObjectId idDueno,ObjectId idMascota, MultipartFile file){
        Dueno dueno = obtenerPorId(idDueno);

        if(dueno == null){
            throw new NullPointerException("El id del dueno no existe");
        }

        String nombreArchivo = "default";
        if (!file.isEmpty()) {
            nombreArchivo = guardarImagenCloudinary(file);
        }

        if (dueno.getMascotas() != null) {
            boolean mascotaEncontrada = false;

            for (Mascota mascota : dueno.getMascotas()) {
                if (mascota.getIdMascota().equals(idMascota)) {
                    mascota.setFotoUrl(nombreArchivo);
                    mascotaEncontrada = true;
                    break;
                }
            }
        }
        Mascota mascota = getMascotaById(idMascota, dueno.getIdDueno());
        mascota.setFotoUrl(nombreArchivo);
        duenoRepository.save(dueno);
    }

    public void guardarMascota(ObjectId idDueno,Mascota mascota, MultipartFile file){
        Dueno dueno = obtenerPorId(idDueno);

        if(dueno == null){
            throw new NullPointerException("El id del dueno no existe");
        }

        List<Mascota> mascotas = dueno.getMascotas();

        String nombreArchivo = "default";
        if (!file.isEmpty()) {
            nombreArchivo = guardarImagenCloudinary(file);
        }
        mascota.setFotoUrl(nombreArchivo);
        mascotas.add(mascota);
        dueno.setMascotas(mascotas);
        duenoRepository.save(dueno);
    }

    public void guardarMascota(String idDueno,Mascota mascota, MultipartFile file){

        Dueno dueno = obtenerPorId(new ObjectId(idDueno));

        if(dueno == null){
            throw new NullPointerException("El id del dueno no existe");
        }

        List<Mascota> mascotas = dueno.getMascotas();

        String nombreArchivo = "default";
        if (!file.isEmpty()) {
            nombreArchivo = guardarImagenCloudinary(file);
        }
        mascota.setFotoUrl(nombreArchivo);
        mascotas.add(mascota);
        dueno.setMascotas(mascotas);
        duenoRepository.save(dueno);
    }


    private String guardarImagenCloudinary(MultipartFile file) {

        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "default.png";
        }
    }


    public void eliminarMascota(ObjectId idDueno, ObjectId idMascota){
        Dueno dueno = obtenerPorId(idDueno);

        if (dueno == null) {
            throw new NullPointerException("El id del dueño no existe");
        }

        if (dueno.getMascotas() != null) {
            dueno.getMascotas().removeIf(mascota -> mascota.getIdMascota().equals(idMascota));
        }

        duenoRepository.save(dueno);
    }

    public void eliminarDueno(ObjectId dueno){

        duenoRepository.deleteById(dueno);
    }

    
}
