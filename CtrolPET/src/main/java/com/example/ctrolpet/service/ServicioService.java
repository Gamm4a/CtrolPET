package com.example.ctrolpet.service;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.ctrolpet.model.Dueno;
import com.example.ctrolpet.model.Mascota;
import com.example.ctrolpet.model.Servicio;
import com.example.ctrolpet.repository.ServicioRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private Cloudinary cloudinary;

    public Servicio guardar(Servicio servicio){

        if (servicio.getDescripcion() == null || servicio.getDescripcion().isBlank()){
            throw new IllegalArgumentException("La descripción no puede estar vacia.");
        }
        if (servicio.getTipo() == null || servicio.getTipo().isBlank()){
            throw new IllegalArgumentException("El tipo no puede estar vacio.");
        }
        if (servicio.getPrecio() == null || servicio.getPrecio() <0){
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }

        if(servicio.getDuracion() == null || servicio.getDuracion() <0){
            throw new IllegalArgumentException("La duración no puede ser negativa.");
        }

        return servicioRepository.save(servicio);

    }

    public List<Servicio> obtenerTodos(){

        return servicioRepository.findAll();

    }

    public Servicio obtenerPorId(ObjectId id){

        return servicioRepository.findById(id).orElse(null);

    }

    public void eliminar(ObjectId id){

        servicioRepository.deleteById(id);

    }

    public Servicio actualizarCompleto(ObjectId id, Servicio servicioActualizado) {

        if (servicioActualizado.getDescripcion() == null || servicioActualizado.getDescripcion().isBlank()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía.");
        }
        if (servicioActualizado.getTipo() == null || servicioActualizado.getTipo().isBlank()) {
            throw new IllegalArgumentException("El tipo no puede estar vacío.");
        }
        if (servicioActualizado.getPrecio() == null || servicioActualizado.getPrecio() < 0) {
            throw new IllegalArgumentException("El precio no puede ser nulo ni negativo.");
        }

        return servicioRepository.findById(id).map(servicio -> {
            servicio.setDescripcion(servicioActualizado.getDescripcion());
            servicio.setTipo(servicioActualizado.getTipo());
            servicio.setPrecio(servicioActualizado.getPrecio());

            return servicioRepository.save(servicio);
        }).orElse(null);
    }

    public Servicio actualizarParcial(ObjectId id, Servicio servicioParcial) {

        return servicioRepository.findById(id).map(servicio -> {

            if (servicioParcial.getDescripcion() != null && !servicioParcial.getDescripcion().isBlank()) {
                servicio.setDescripcion(servicioParcial.getDescripcion());
            }

            if (servicioParcial.getTipo() != null && !servicioParcial.getTipo().isBlank()) {
                servicio.setTipo(servicioParcial.getTipo());
            }


            if (servicioParcial.getPrecio() != null) {
                if (servicioParcial.getPrecio() < 0) {
                    throw new IllegalArgumentException("El precio modificado no puede ser negativo.");
                }
                servicio.setPrecio(servicioParcial.getPrecio());
            }

            if (servicioParcial.getDuracion() != null) {
                if (servicioParcial.getDuracion() < 0) {
                    throw new IllegalArgumentException("La duración no puede ser negativa.");
                }
                servicio.setDuracion(servicioParcial.getDuracion());
            }

            return servicioRepository.save(servicio);
        }).orElse(null);
    }


    public List<LocalTime> rangoCitas(LocalTime entrada, LocalTime salida, Integer duracionMinutos) {
        List<LocalTime> intervalos = new ArrayList<>();
        LocalTime tiempoActual = entrada;

        while (tiempoActual.plusMinutes(duracionMinutos).isBefore(salida) || tiempoActual.plusMinutes(duracionMinutos).equals(salida)) {
            intervalos.add(tiempoActual);
            tiempoActual = tiempoActual.plusMinutes(duracionMinutos);
        }

        return intervalos;
    }

    public void guardarImagenesServicios(ObjectId idServicio, MultipartFile file){
        Servicio servicio = obtenerPorId(idServicio);

        if(servicio == null){
            throw new NullPointerException("El id del servicio no existe");
        }

        String nombreArchivo = "default";
        if (!file.isEmpty()) {
            nombreArchivo = guardarImagenCloudinary(file);
        }

        if (servicio.getFotos() == null) {
            servicio.setFotos(new ArrayList<>());
        }

        servicio.getFotos().add(nombreArchivo);
        servicioRepository.save(servicio);
    }

    public void eliminarImagenServicio(ObjectId idServicio, String fotoUrl) {
        Servicio servicio = obtenerPorId(idServicio);

        if (servicio == null) {
            throw new NullPointerException("El id del servicio no existe");
        }

        if (servicio.getFotos() != null) {
            servicio.getFotos().remove(fotoUrl);

            servicioRepository.save(servicio);
        }
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

}

