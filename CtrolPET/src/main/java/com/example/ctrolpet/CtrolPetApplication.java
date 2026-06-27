package com.example.ctrolpet;

import com.example.ctrolpet.model.Empleado;
import com.example.ctrolpet.model.Enum.DiaSemana;
import com.example.ctrolpet.model.Enum.Puesto;
import com.example.ctrolpet.model.Horario;
import com.example.ctrolpet.repository.EmpleadosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalTime;
import java.util.Set;

@SpringBootApplication
public class CtrolPetApplication {

    public static void main(String[] args) {
        SpringApplication.run(CtrolPetApplication.class, args);
    }

    @Autowired
    EmpleadosRepository empleadosRepository;

    @Bean
    public CommandLineRunner empleadoMockeado() {
        return args -> {
            Empleado empleado = new Empleado();
            empleado.setNombre("Sandra");
            empleado.setApellidoPaterno("Armenta");
            empleado.setApellidoMaterno("Diaz");
            empleado.setCorreo("lasandra@hotmail.com");
            empleado.setContrasenia("pass123");
            empleado.setTelefono("6441212121");
            empleado.setPuesto(Puesto.ADMINISTRADOR);

            Set<DiaSemana> dias = Set.of(DiaSemana.LUNES, DiaSemana.MIERCOLES, DiaSemana.VIERNES);

            Horario horario = new Horario();
            horario.setDias(dias);
            horario.setHoraEntrada(LocalTime.of(8,00,00));
            horario.setHoraSalida(LocalTime.of(17,00,00));
            empleado.setHorarios(horario);

            Empleado empleadoBuscado= empleadosRepository.findByCorreo(empleado.getCorreo()).orElse(null);
            if (empleadoBuscado == null) {
                empleadosRepository.save(empleado);
            }

        };
    }


}
