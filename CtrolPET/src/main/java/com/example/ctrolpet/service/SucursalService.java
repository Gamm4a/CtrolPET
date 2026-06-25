package com.example.ctrolpet.service;


import com.example.ctrolpet.model.Sucursal;
import com.example.ctrolpet.repository.SucursalRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class SucursalService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SucursalRepository sucursalRepository;

    public Sucursal guardar(Sucursal sucursal) {

        if (sucursal.getNombre() == null || sucursal.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la sucursal es obligatorio.");
        }
        if (sucursal.getDireccion() == null) {
            throw new IllegalArgumentException("La dirección de la sucursal es obligatoria.");
        }
        if (sucursal.getTelefono() == null || !sucursal.getTelefono().matches("^\\d{10}$")) {
            throw new IllegalArgumentException("El teléfono debe contener exactamente 10 dígitos numéricos.");
        }
        if (sucursal.getCorreo() == null || sucursal.getCorreo().isBlank()) {
            throw new IllegalArgumentException("El correo electrónico es obligatorio.");
        }
        if (sucursal.getPassword() == null || sucursal.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }

        //La sucursar tiene contraseña?
        sucursal.setPassword(passwordEncoder.encode(sucursal.getPassword()));

        return sucursalRepository.save(sucursal);
    }

    public List<Sucursal> obtenerTodos() {

        return sucursalRepository.findAll();

    }

    public Sucursal obtenerPorId(ObjectId id) {

        return sucursalRepository.findById(id).orElse(null);

    }

    public void eliminar(ObjectId id) {

        sucursalRepository.deleteById(id);

    }

    public Sucursal actualizarCompleto(ObjectId id, Sucursal sucursalActualizada) {
        // 1. Validar todos los campos del reemplazo completo
        if (sucursalActualizada.getNombre() == null || sucursalActualizada.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la sucursal es obligatorio.");
        }
        if (sucursalActualizada.getDireccion() == null) {
            throw new IllegalArgumentException("La dirección de la sucursal es obligatoria.");
        }
        if (sucursalActualizada.getTelefono() == null || !sucursalActualizada.getTelefono().matches("^\\d{10}$")) {
            throw new IllegalArgumentException("El teléfono debe contener exactamente 10 dígitos numéricos.");
        }
        if (sucursalActualizada.getCorreo() == null || sucursalActualizada.getCorreo().isBlank()) {
            throw new IllegalArgumentException("El correo electrónico es obligatorio.");
        }
        if (sucursalActualizada.getPassword() == null || sucursalActualizada.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }

        //La sucursar tiene contraseña?
        sucursalActualizada.setPassword(passwordEncoder.encode(sucursalActualizada.getPassword()));


        return sucursalRepository.findById(id).map(sucursal -> {
            sucursal.setNombre(sucursalActualizada.getNombre());
            sucursal.setDireccion(sucursalActualizada.getDireccion());
            sucursal.setTelefono(sucursalActualizada.getTelefono());
            sucursal.setCorreo(sucursalActualizada.getCorreo());
            sucursal.setPassword(sucursalActualizada.getPassword());
            sucursal.setEmpleados(sucursalActualizada.getEmpleados()); // Se reemplaza la lista completa

            return sucursalRepository.save(sucursal);
        }).orElse(null);
    }

    public Sucursal actualizarParcial(ObjectId id, Sucursal sucursalParcial) {
        return sucursalRepository.findById(id).map(sucursal -> {

            if (sucursalParcial.getNombre() != null && !sucursalParcial.getNombre().isBlank()) {
                sucursal.setNombre(sucursalParcial.getNombre());
            }

            if (sucursalParcial.getDireccion() != null) {
                sucursal.setDireccion(sucursalParcial.getDireccion());
            }

            if (sucursalParcial.getTelefono() != null) {
                if (!sucursalParcial.getTelefono().matches("^\\d{10}$")) {
                    throw new IllegalArgumentException("El teléfono modificado debe contener exactamente 10 dígitos.");
                }
                sucursal.setTelefono(sucursalParcial.getTelefono());
            }

            if (sucursalParcial.getCorreo() != null && !sucursalParcial.getCorreo().isBlank()) {
                sucursal.setCorreo(sucursalParcial.getCorreo());
            }

            if (sucursalParcial.getPassword() != null && !sucursalParcial.getPassword().isBlank()) {
                //La sucursar tiene contraseña?
                sucursalParcial.setPassword(passwordEncoder.encode(sucursalParcial.getPassword()));
                sucursal.setPassword(sucursalParcial.getPassword());
            }

            if (sucursalParcial.getEmpleados() != null) {
                sucursal.setEmpleados(sucursalParcial.getEmpleados());
            }

            return sucursalRepository.save(sucursal);
        }).orElse(null);
    }

}
