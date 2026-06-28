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


        };
    }


}
