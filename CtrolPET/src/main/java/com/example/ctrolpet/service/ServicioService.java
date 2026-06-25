package com.example.ctrolpet.service;


import com.example.ctrolpet.model.Servicio;
import com.example.ctrolpet.repository.ServicioRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

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

            return servicioRepository.save(servicio);
        }).orElse(null);
    }


}
