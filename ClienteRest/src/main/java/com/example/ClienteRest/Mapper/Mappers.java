package com.example.ClienteRest.Mapper;

import com.example.ClienteRest.dtos.*;
import com.example.ClienteRest.dtos.Enum.DiaSemanaDTO;
import com.example.ClienteRest.dtos.Enum.EspecialidadDTO;
import com.example.ClienteRest.dtos.Enum.EstadoReservaDTO;
import com.example.ClienteRest.dtos.Enum.PuestoDTO;
import com.example.ctrolpet.model.*;
import com.example.ctrolpet.model.Enum.DiaSemana;
import com.example.ctrolpet.model.Enum.Especialidad;
import com.example.ctrolpet.model.Enum.EstadoReserva;
import com.example.ctrolpet.model.Enum.Puesto;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class Mappers {

    public static DireccionDTO ToDTO(Direccion entity) {
        DireccionDTO direccionDTO = new DireccionDTO();
        direccionDTO.setCalle(entity.getCalle());
        direccionDTO.setNumeroCasa(entity.getNumeroCasa());
        direccionDTO.setColonia(entity.getColonia());
        direccionDTO.setEstado(entity.getEstado());
        direccionDTO.setCiudad(entity.getCiudad());
        direccionDTO.setEstado(entity.getEstado());
        direccionDTO.setCodigoPostal(entity.getCodigoPostal());
        direccionDTO.setEstado(entity.getEstado());

        return direccionDTO;
    }

    public static EspecialidadDTO toDTO(Especialidad entity) {
        return EspecialidadDTO.valueOf(entity.name());
    }

    public static DiaSemanaDTO toDTO(DiaSemana entity) {
        return DiaSemanaDTO.valueOf(entity.name());
    }

    public static EstadoReservaDTO toDTO(EstadoReserva entity) {
        return EstadoReservaDTO.valueOf(entity.name());
    }

    public static PuestoDTO toDTO(Puesto entity) {
        return PuestoDTO.valueOf(entity.name());
    }

    public static DuenoDTO toDTO(Dueno entity) {
        DuenoDTO duenoDTO = new DuenoDTO();

        if (entity.getIdDueno() != null) {
            duenoDTO.setIdDueno(entity.getIdDueno().toString());
        }

        duenoDTO.setNombre(entity.getNombre());
        duenoDTO.setApellidoPaterno(entity.getApellidoPaterno());
        duenoDTO.setApellidoMaterno(entity.getApellidoMaterno());
        duenoDTO.setCorreo(entity.getCorreo());
        duenoDTO.setContrasenia(entity.getContrasenia());
        duenoDTO.setFechaNacimiento(entity.getFechaNacimiento());
        duenoDTO.setTelefono(entity.getTelefono());
        duenoDTO.setDireccion(ToDTO(entity.getDireccion()));
        List<MascotaDTO> mascotasDTOList = new ArrayList<>();
        if (entity.getMascotas() != null && !entity.getMascotas().isEmpty()) {
            for (Mascota m : entity.getMascotas()) {
                mascotasDTOList.add(Mappers.toDTO(m));
            }
            duenoDTO.setMascotas(mascotasDTOList);
        }

        return duenoDTO;
    }



    public static HorarioDTO toDTO(Horario entity) {
        HorarioDTO horarioDTO = new HorarioDTO();
        for (DiaSemana dia : entity.getDias()) {
            horarioDTO.getDias().add(toDTO(dia));
        }
        horarioDTO.setHoraEntrada(entity.getHoraEntrada());
        horarioDTO.setHoraSalida(entity.getHoraSalida());
        return horarioDTO;
    }

    public static EmpleadoDTO toDTO(Empleado entity) {
        EmpleadoDTO empleadoDTO = new EmpleadoDTO();
        empleadoDTO.setIdEmpleado(entity.getIdEmpleado().toString());
        empleadoDTO.setNombre(entity.getNombre());
        empleadoDTO.setApellidoPaterno(entity.getApellidoPaterno());
        empleadoDTO.setApellidoMaterno(entity.getApellidoMaterno());
        empleadoDTO.setSucursal(entity.getSucursal().toString());
        empleadoDTO.setEspecialidad(Mappers.toDTO(entity.getEspecialidad()));
        empleadoDTO.setCorreo(entity.getCorreo());
        empleadoDTO.setTelefono(entity.getTelefono());
        empleadoDTO.setPuesto(Mappers.toDTO(entity.getPuesto()));
        empleadoDTO.setHorarios(Mappers.toDTO(entity.getHorarios()));
        return empleadoDTO;
    }

    public static SucursalDTO toDTO(Sucursal entity){
    SucursalDTO sucursalDTO = new SucursalDTO();
    sucursalDTO.setEmpleados(new ArrayList<>());
    sucursalDTO.setIdSucursal(entity.getIdSucursal().toString());
    sucursalDTO.setNombre(entity.getNombre());
    if (entity.getDireccion() != null) {
        sucursalDTO.setDireccion(Mappers.ToDTO(entity.getDireccion()));
    }
    sucursalDTO.setTelefono(entity.getTelefono());
        if (entity.getEmpleados() != null) {
            for (ObjectId empleado : entity.getEmpleados()) {
                if (empleado != null) {
                    sucursalDTO.getEmpleados().add(empleado.toString());
                }
            }
        }
    return sucursalDTO;


    }

    public static ServicioDTO toDTO(Servicio entity) {
        ServicioDTO servicioDTO = new ServicioDTO();
        servicioDTO.setIdServicio(entity.getIdServicio().toString());
        servicioDTO.setTipo(entity.getTipo());
        servicioDTO.setDescripcion(entity.getDescripcion());
        servicioDTO.setPrecio(entity.getPrecio());
        servicioDTO.setCategoria(Mappers.toDTO(entity.getCategoria()));
        servicioDTO.setDuracion(entity.getDuracion());
        servicioDTO.setFotos(entity.getFotos());
        return servicioDTO;
    }

    public static ReservaDTO toDTO(Reserva entity) {
        ReservaDTO reservaDTO = new ReservaDTO();
        reservaDTO.setIdReserva(entity.getIdReserva().toString());
        reservaDTO.setIdEmpleado(entity.getIdEmpleado().toString());
        reservaDTO.setFecha(entity.getFecha());
        reservaDTO.setEstado(Mappers.toDTO(entity.getEstado()));
        reservaDTO.setIdSucursal(entity.getIdSucursal().toString());
        if (entity.getDueno() != null) {
            reservaDTO.setDueno(entity.getDueno().toString());
        }
        if (entity.getMascota() != null) {
            reservaDTO.setMascota(entity.getMascota().toString());
        }
        reservaDTO.setServicios(Mappers.toDTO(entity.getServicios()));
        return reservaDTO;
    }

    public static MedicamentoDTO toDTO(Medicamento entity) {
        MedicamentoDTO medicamentoDTO = new MedicamentoDTO();
        medicamentoDTO.setIdMedicamento(entity.getIdMedicmento().toString());
        medicamentoDTO.setNombre(entity.getNombre());
        medicamentoDTO.setDosis(entity.getDosis());
        medicamentoDTO.setFrecuencia(entity.getFrecuencia());
        medicamentoDTO.setDuracion(entity.getDuracion());
        return medicamentoDTO;
    }

    public static HistorialClinicoDTO toDTO(HistorialClinico entity) {
        HistorialClinicoDTO historialClinicoDTO = new HistorialClinicoDTO();
        historialClinicoDTO.setIdHistorialClinico(entity.getIdHistorialClinico().toString());
        historialClinicoDTO.setIdMascota(entity.getIdMascota().toString());
        historialClinicoDTO.setFecha(entity.getFecha());
        historialClinicoDTO.setDiagnostico(entity.getDiagnostico());
        historialClinicoDTO.setObservaciones(entity.getObservaciones());
        for (Medicamento medicamento : entity.getMedicamentos()) {
            historialClinicoDTO.getMedicamentos().add(toDTO(medicamento));
        }
        return historialClinicoDTO;
    }

    public static MascotaDTO toDTO(Mascota entity) {
        MascotaDTO mascotaDTO = new MascotaDTO();
        if (entity.getIdMascota() != null) {
            mascotaDTO.setIdMascota(entity.getIdMascota().toString());
        }
        mascotaDTO.setNombre(entity.getNombre());
        mascotaDTO.setEspecie(entity.getEspecie());
        mascotaDTO.setRaza(entity.getRaza());
        mascotaDTO.setFechaNacimiento(entity.getFechaNacimiento());
        mascotaDTO.setFotoUrl(entity.getFotoUrl());
        if (entity.getIdHistorialClinico() != null) {
            mascotaDTO.setIdHistorialClinico(entity.getIdHistorialClinico().toString());
        }
        return mascotaDTO;
    }

    public static Direccion toEntity(DireccionDTO dto) {
        Direccion direccion = new Direccion();
        direccion.setCalle(dto.getCalle());
        direccion.setNumeroCasa(dto.getNumeroCasa());
        direccion.setColonia(dto.getColonia());
        direccion.setCiudad(dto.getCiudad());
        direccion.setEstado(dto.getEstado());
        direccion.setCodigoPostal(dto.getCodigoPostal());
        return direccion;
    }

    public static Especialidad toEntity(EspecialidadDTO dto) {
        return Especialidad.valueOf(dto.name());
    }

    public static DiaSemana toEntity(DiaSemanaDTO dto) {
        return DiaSemana.valueOf(dto.name());
    }

    public static EstadoReserva toEntity(EstadoReservaDTO dto) {
        return EstadoReserva.valueOf(dto.name());
    }

    public static Puesto toEntity(PuestoDTO dto) {
        return Puesto.valueOf(dto.name());
    }

    public static Dueno toEntity(DuenoDTO dto) {
        Dueno dueno = new Dueno();

        if (dto.getIdDueno() != null) {
            dueno.setIdDueno(new ObjectId(dto.getIdDueno()));
        }
        dueno.setNombre(dto.getNombre());
        dueno.setApellidoPaterno(dto.getApellidoPaterno());
        dueno.setApellidoMaterno(dto.getApellidoMaterno());
        dueno.setCorreo(dto.getCorreo());
        dueno.setContrasenia(dto.getContrasenia());
        dueno.setFechaNacimiento(dto.getFechaNacimiento());
        dueno.setTelefono(dto.getTelefono());
        dueno.setDireccion(toEntity(dto.getDireccion()));
        if (dto.getMascotas() != null) {
            List<Mascota> mascotas = new ArrayList<>();
            for (MascotaDTO mascotaDTO : dto.getMascotas()) {
                mascotas.add(toEntity(mascotaDTO));
            }
            dueno.setMascotas(mascotas);
        }
        return dueno;
    }

    public static Dueno toEntityUpdate(DuenoDTO dto) {
        Dueno dueno = new Dueno();

        if (dto.getNombre() != null) dueno.setNombre(dto.getNombre());
        if (dto.getApellidoPaterno() != null) dueno.setApellidoPaterno(dto.getApellidoPaterno());
        if (dto.getApellidoMaterno() != null) dueno.setApellidoMaterno(dto.getApellidoMaterno());
        if (dto.getCorreo() != null) dueno.setCorreo(dto.getCorreo());
        if (dto.getTelefono() != null) dueno.setTelefono(dto.getTelefono());

        return dueno;
    }

    public static Horario toEntity(HorarioDTO dto) {
        Horario horario = new Horario();
        for (DiaSemanaDTO diaDTO : dto.getDias()) {
            horario.getDias().add(toEntity(diaDTO));
        }
        horario.setHoraEntrada(dto.getHoraEntrada());
        horario.setHoraSalida(dto.getHoraSalida());
        return horario;
    }

    public static Empleado toEntity(EmpleadoDTO dto) {
        Empleado empleado = new Empleado();
        empleado.setIdEmpleado(new ObjectId(dto.getIdEmpleado()));
        empleado.setNombre(dto.getNombre());
        empleado.setApellidoPaterno(dto.getApellidoPaterno());
        empleado.setApellidoMaterno(dto.getApellidoMaterno());
        empleado.setSucursal(new ObjectId(dto.getSucursal())); // suponiendo que es un ObjectId
        empleado.setEspecialidad(toEntity(dto.getEspecialidad()));
        empleado.setCorreo(dto.getCorreo());
        empleado.setTelefono(dto.getTelefono());
        empleado.setPuesto(toEntity(dto.getPuesto()));
        empleado.setHorarios(toEntity(dto.getHorarios()));
        return empleado;
    }

    public static Sucursal toEntity(SucursalDTO dto) {
        Sucursal sucursal = new Sucursal();
        sucursal.setIdSucursal(new ObjectId(dto.getIdSucursal()));
        sucursal.setNombre(dto.getNombre());
        sucursal.setDireccion(toEntity(dto.getDireccion()));
        sucursal.setTelefono(dto.getTelefono());
        for (String empleadoId : dto.getEmpleados()) {
            sucursal.getEmpleados().add(new ObjectId(empleadoId));
        }
        return sucursal;
    }

    public static Servicio toEntity(ServicioDTO dto) {
        Servicio servicio = new Servicio();
        servicio.setIdServicio(new ObjectId(dto.getIdServicio()));
        servicio.setTipo(dto.getTipo());
        servicio.setDescripcion(dto.getDescripcion());
        servicio.setPrecio(dto.getPrecio());
        servicio.setCategoria(toEntity(dto.getCategoria()));
        servicio.setDuracion(dto.getDuracion());
        return servicio;
    }

    public static Reserva toEntity(ReservaDTO dto) {
        Reserva reserva = new Reserva();
        if (dto.getIdReserva() != null && !dto.getIdReserva().isBlank()) {
            reserva.setIdReserva(new ObjectId(dto.getIdReserva()));
        }
        reserva.setIdEmpleado(new ObjectId(dto.getIdEmpleado()));
        if (dto.getDueno() != null && !dto.getDueno().isBlank()) {
            reserva.setDueno(new ObjectId(dto.getDueno()));
        }

        if (dto.getMascota() != null && !dto.getMascota().isBlank()) {
            reserva.setMascota(new ObjectId(dto.getMascota()));
        }
        reserva.setFecha(dto.getFecha());
        reserva.setEstado(toEntity(dto.getEstado()));
        reserva.setIdSucursal(new ObjectId(dto.getIdSucursal()));

        reserva.setServicios(toEntity(dto.getServicios()));

        return reserva;
    }

    public static Medicamento toEntity(MedicamentoDTO dto) {
        Medicamento medicamento = new Medicamento();
        medicamento.setIdMedicmento(new ObjectId(dto.getIdMedicamento()));
        medicamento.setNombre(dto.getNombre());
        medicamento.setDosis(dto.getDosis());
        medicamento.setFrecuencia(dto.getFrecuencia());
        medicamento.setDuracion(dto.getDuracion());
        return medicamento;
    }

    public static HistorialClinico toEntity(HistorialClinicoDTO dto) {
        HistorialClinico historial = new HistorialClinico();
        historial.setIdHistorialClinico(new ObjectId(dto.getIdHistorialClinico()));
        historial.setIdMascota(new ObjectId(dto.getIdMascota()));
        historial.setFecha(dto.getFecha());
        historial.setDiagnostico(dto.getDiagnostico());
        historial.setObservaciones(dto.getObservaciones());
        for (MedicamentoDTO medicamentoDTO : dto.getMedicamentos()) {
            historial.getMedicamentos().add(toEntity(medicamentoDTO));
        }
        return historial;
    }

    public static Mascota toEntity(MascotaDTO dto) {
        Mascota mascota = new Mascota();
        if (dto.getIdMascota() != null && !dto.getIdMascota().isBlank()) {
            mascota.setIdMascota(new ObjectId(dto.getIdMascota()));
        }
        mascota.setNombre(dto.getNombre());
        mascota.setEspecie(dto.getEspecie());
        mascota.setRaza(dto.getRaza());
        mascota.setFechaNacimiento(dto.getFechaNacimiento());
        mascota.setFotoUrl(dto.getFotoUrl());
        if (dto.getIdHistorialClinico() != null && !dto.getIdHistorialClinico().isBlank()) {
            mascota.setIdHistorialClinico(new ObjectId(dto.getIdHistorialClinico()));
        }
        return mascota;
    }

}