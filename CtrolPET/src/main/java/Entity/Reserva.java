package Entity;


import Entity.Enum.EstadoReserva;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.time.LocalDateTime;

public class Reserva {
    ObjectId id_Reserva;
    ObjectId id_Empleado;
    LocalDateTime fecha;
    EstadoReserva estado;
    Mascota mascota;
    Servicio servicio;

    public Reserva(ObjectId id_Reserva,ObjectId id_Empleado,LocalDateTime fecha, EstadoReserva estado, Mascota mascota, Servicio servicio) {
        this.id_Reserva=id_Reserva;
        this.id_Empleado=id_Empleado;
        this.fecha = fecha;
        this.estado = estado;
        this.mascota = mascota;
        this.servicio = servicio;
    }

    public Reserva() {
    }

    public ObjectId getId_Reserva() {
        return id_Reserva;
    }

    public void setId_Reserva(ObjectId id_Reserva) {
        this.id_Reserva = id_Reserva;
    }

    public ObjectId getId_Empleado() {
        return id_Empleado;
    }

    public void setId_Empleado(ObjectId id_Empleado) {
        this.id_Empleado = id_Empleado;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }
}
