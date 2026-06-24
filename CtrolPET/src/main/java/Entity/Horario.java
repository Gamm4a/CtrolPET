package Entity;

import Entity.Enum.DiaSemana;

import java.time.LocalTime;

public class Horario {
    DiaSemana dia;
    LocalTime hora_entrada;
    LocalTime hora_salida;

    public Horario() {
    }

    public Horario(DiaSemana dia, LocalTime hora_entrada, LocalTime hora_salida) {
        this.dia = dia;
        this.hora_entrada = hora_entrada;
        this.hora_salida = hora_salida;
    }

    public DiaSemana getDia() {
        return dia;
    }

    public void setDia(DiaSemana dia) {
        this.dia = dia;
    }

    public LocalTime getHora_entrada() {
        return hora_entrada;
    }

    public void setHora_entrada(LocalTime hora_entrada) {
        this.hora_entrada = hora_entrada;
    }

    public LocalTime getHora_salida() {
        return hora_salida;
    }

    public void setHora_salida(LocalTime hora_salida) {
        this.hora_salida = hora_salida;
    }
}
