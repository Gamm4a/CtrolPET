package com.example.ctrolpet.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.ctrolpet.exception.BadCredentialsException;
import com.example.ctrolpet.exception.EmailDuplicateException;
import com.example.ctrolpet.exception.ResourceNotFoundException;
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
import java.time.LocalDate;
import java.time.ZoneId;
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

    public Dueno autenticar(String correo, String contrasenia) throws BadCredentialsException {
        Optional<Dueno> dueñoOptional = duenoRepository.findByCorreo(correo);

        if(!dueñoOptional.isPresent()) {
            throw new BadCredentialsException("Email no registrado");
        }

        Dueno dueno = dueñoOptional.get();

        if(!contrasenia.matches(dueno.getContrasenia())){
            throw new BadCredentialsException("Contraseña incorrecta");
        }

        return dueno;
//            if(passwordEncoder.matches(contrasenia, dueno.getContrasenia())){
//                return dueno;
//            }

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

    public Dueno obtenerPorId(ObjectId id) throws ResourceNotFoundException {

        return duenoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un dueño con el ID especificado"));

    }

    public Dueno guardar(Dueno dueno)throws EmailDuplicateException {

        if(dueno.getCorreo() != null){
        Optional<Dueno> duenoExistente = duenoRepository.findByCorreo(dueno.getCorreo());
        if(duenoExistente.isPresent()){
            throw new EmailDuplicateException("Este email ya está registrado");
        }
    }
        return duenoRepository.save(dueno);

    }

    public void actualizarDatosDueno(ObjectId idDueno, String nombre, String apellidoPaterno, String apellidoMaterno, String telefono, String correo, LocalDate fechaNacimiento){
        Dueno dueno = obtenerPorId(idDueno);

        if (dueno == null) {
            throw new NullPointerException("El id del dueño no existe");
        }

        Instant instant = fechaNacimiento.atStartOfDay(ZoneId.systemDefault()).toInstant();
        dueno.setFechaNacimiento(instant);
        if (nombre != null){
            dueno.setNombre(nombre);
        }

        if (apellidoPaterno != null){
            dueno.setApellidoPaterno(apellidoPaterno);
        }

        if (apellidoMaterno != null){
            dueno.setApellidoMaterno(apellidoMaterno);
        }

        if (telefono != null){
            dueno.setTelefono(telefono);
        }

        if (correo != null){
            dueno.setCorreo(correo);
        }


        duenoRepository.save(dueno);
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

    public void actualizarDatosMascota(ObjectId idDueno, ObjectId idMascota, String nombre, String especie, String raza, LocalDate fechaNacimiento){
        Dueno dueno = obtenerPorId(idDueno);

        if (dueno == null) {
            throw new NullPointerException("El id del dueño no existe");
        }

        Instant instant = fechaNacimiento.atStartOfDay(ZoneId.systemDefault()).toInstant();
        if (dueno.getMascotas() != null) {
            boolean encontrado = false;
            for (Mascota mascota : dueno.getMascotas()) {
                if (mascota.getIdMascota().equals(idMascota)) {
                    if (nombre != null) {
                        mascota.setNombre(nombre);
                    }
                    if (especie != null) {
                        mascota.setEspecie(especie);
                    }
                    if (raza != null) {
                        mascota.setRaza(raza);
                    }
                    mascota.setFechaNacimiento(instant);
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                throw new NullPointerException("La mascota no pertenece a este dueño");
            }
        }
        duenoRepository.save(dueno);
    }
    public Dueno actualizarDueno(ObjectId idDueno, Dueno duenoActualizado) {

        Optional<Dueno> duenoOpcional = duenoRepository.findById(idDueno);

        if (duenoOpcional.isPresent()) {
            Dueno duenoExistente = duenoOpcional.get();

            // 2. Actualizamos campo por campo los datos modificables
            if (duenoExistente.getNombre() != null){
                duenoExistente.setNombre(duenoActualizado.getNombre());
            }

            if (duenoExistente.getApellidoPaterno() != null){
                duenoExistente.setApellidoPaterno(duenoActualizado.getApellidoPaterno());
            }

            if (duenoExistente.getApellidoMaterno() != null){
                duenoExistente.setApellidoMaterno(duenoActualizado.getApellidoMaterno());
            }

            if (duenoExistente.getCorreo() != null){
                duenoExistente.setCorreo(duenoActualizado.getCorreo());
            }

            if (duenoExistente.getTelefono() != null){
                duenoExistente.setTelefono(duenoActualizado.getTelefono());
            }

            if (duenoActualizado.getDireccion() != null) {
                duenoExistente.setDireccion(duenoActualizado.getDireccion());
            }


            return duenoRepository.save(duenoExistente);
        }


        return null;
    }




}
