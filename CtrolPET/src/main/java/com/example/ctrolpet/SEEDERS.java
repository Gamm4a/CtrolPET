package com.example.ctrolpet;

import com.example.ctrolpet.model.*;
import com.example.ctrolpet.model.Enum.*;
import com.example.ctrolpet.repository.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class SEEDERS {

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private EmpleadosRepository empleadosRepository;

    @Autowired
    private DuenoRepository duenoRepository;

    @Autowired
    private HistorialClinicoRepository historialClinicoRepository;

    @Autowired
    private ReservaRepository reservaRepository;


    @Bean
    public CommandLineRunner seedDatabase() {
        return args -> {
            // Limpiar las colecciones
            /* ELIMINAR TODOS LOS DATOS SI NECESITAS REINICIAR TUS SEEDERS
            servicioRepository.deleteAll();
            sucursalRepository.deleteAll();
            empleadosRepository.deleteAll();
            duenoRepository.deleteAll();
            historialClinicoRepository.deleteAll();
            reservaRepository.deleteAll();
            */

            if (servicioRepository.count() == 0 &&
                    sucursalRepository.count() == 0 &&
                    empleadosRepository.count() == 0 &&
                    duenoRepository.count() == 0 &&
                    historialClinicoRepository.count() == 0 &&
                    reservaRepository.count() == 0) {


                System.out.println("🌱 Iniciando SEEDER...");

                // 1. SERVICIOS (5 servicios)
                List<Servicio> servicios = seedServicios();
                System.out.println("✅ " + servicios.size() + " servicios creados");

                // 2. SUCURSALES (2 sucursales)
                List<Sucursal> sucursales = seedSucursales();
                System.out.println("✅ " + sucursales.size() + " sucursales creadas");

                // 3. EMPLEADOS (7 empleados NO ADMINISTRADORES)
                List<Empleado> empleados = seedEmpleados(sucursales);
                System.out.println("✅ " + empleados.size() + " empleados creados");

                // Actualizar sucursales con empleados
                actualizarSucursalesConEmpleados(sucursales, empleados);

                // 4. DUEÑOS CON MASCOTAS E HISTORIALES CLÍNICOS (10 dueños, cada uno con 1-3 mascotas)
                List<Dueno> duenos = seedDuenosConMascotas();
                System.out.println("✅ " + duenos.size() + " dueños creados con sus mascotas");

                // 5. RESERVAS (algunas reservas de ejemplo)
                List<Reserva> reservas = seedReservas(duenos, empleados, sucursales, servicios);
                System.out.println("✅ " + reservas.size() + " reservas creadas");

                System.out.println("🎉 SEEDER completado exitosamente!");
            }
        };
    }

    private List<Servicio> seedServicios() {
        List<Servicio> servicios = new ArrayList<>();

        servicios.add(new Servicio(null, "Consulta General", "Revisión médica general de la mascota", 300.0, Especialidad.CONSULTA, 30, new ArrayList<>()));
        servicios.add(new Servicio(null, "Limpieza Dental", "Limpieza dental completa con ultrasonido", 800.0, Especialidad.DENTAL, 60, new ArrayList<>()));
        servicios.add(new Servicio(null, "Baño y Corte", "Servicio de estética completo", 350.0, Especialidad.ESTETICA, 90, new ArrayList<>()));
        servicios.add(new Servicio(null, "Radiografía", "Estudio radiográfico diagnóstico", 500.0, Especialidad.DIAGNOSTICO, 45, new ArrayList<>()));
        servicios.add(new Servicio(null, "Esterilización", "Cirugía de esterilización", 1500.0, Especialidad.CIRUGIA, 120, new ArrayList<>()));

        return servicioRepository.saveAll(servicios);
    }

    private List<Sucursal> seedSucursales() {
        List<Sucursal> sucursales = new ArrayList<>();

        Direccion dir1 = new Direccion("Av. Juárez", "Centro", "Hermosillo", "Sonora", "83000", "123");
        sucursales.add(new Sucursal(null, "CtrolPET Centro", dir1, "6621234567", new ArrayList<>()));

        Direccion dir2 = new Direccion("Blvd. Luis Encinas", "San Benito", "Hermosillo", "Sonora", "83190", "456");
        sucursales.add(new Sucursal(null, "CtrolPET Norte", dir2, "6629876543", new ArrayList<>()));

        return sucursalRepository.saveAll(sucursales);
    }

    private List<Empleado> seedEmpleados(List<Sucursal> sucursales) {
        List<Empleado> empleados = new ArrayList<>();
        ObjectId sucursal1 = sucursales.get(0).getIdSucursal();
        ObjectId sucursal2 = sucursales.get(1).getIdSucursal();

        // Empleado 1 - Veterinario
        Horario horario1 = new Horario(Set.of(DiaSemana.LUNES, DiaSemana.MIERCOLES, DiaSemana.VIERNES),
                LocalTime.of(8, 0), LocalTime.of(16, 0));
        empleados.add(new Empleado(null, "Carlos", "Mendoza", "López", sucursal1, Especialidad.CONSULTA,
                "pass123", "carlos.mendoza@ctrolpet.com", "6621111111", Puesto.EMPLEADO, horario1));

        // Empleado 2 - Veterinario Dental
        Horario horario2 = new Horario(Set.of(DiaSemana.MARTES, DiaSemana.JUEVES, DiaSemana.SABADO),
                LocalTime.of(9, 0), LocalTime.of(17, 0));
        empleados.add(new Empleado(null, "María", "Rodríguez", "García", sucursal1, Especialidad.DENTAL,
                "pass123", "maria.rodriguez@ctrolpet.com", "6622222222", Puesto.EMPLEADO, horario2));

        // Empleado 3 - Estilista
        Horario horario3 = new Horario(Set.of(DiaSemana.LUNES, DiaSemana.MARTES, DiaSemana.MIERCOLES, DiaSemana.JUEVES),
                LocalTime.of(10, 0), LocalTime.of(18, 0));
        empleados.add(new Empleado(null, "Ana", "Hernández", "Soto", sucursal2, Especialidad.ESTETICA,
                "pass123", "ana.hernandez@ctrolpet.com", "6623333333", Puesto.EMPLEADO, horario3));

        // Empleado 4 - Diagnóstico
        Horario horario4 = new Horario(Set.of(DiaSemana.LUNES, DiaSemana.MIERCOLES, DiaSemana.VIERNES, DiaSemana.SABADO),
                LocalTime.of(7, 0), LocalTime.of(15, 0));
        empleados.add(new Empleado(null, "Luis", "Pérez", "Martínez", sucursal2, Especialidad.DIAGNOSTICO,
                "pass123", "luis.perez@ctrolpet.com", "6624444444", Puesto.EMPLEADO, horario4));

        // Empleado 5 - Cirujano
        Horario horario5 = new Horario(Set.of(DiaSemana.MARTES, DiaSemana.JUEVES),
                LocalTime.of(8, 0), LocalTime.of(16, 0));
        empleados.add(new Empleado(null, "Jorge", "Ramírez", "Cruz", sucursal1, Especialidad.CIRUGIA,
                "pass123", "jorge.ramirez@ctrolpet.com", "6625555555", Puesto.EMPLEADO, horario5));

        // Empleado 6 - Veterinario General
        Horario horario6 = new Horario(Set.of(DiaSemana.LUNES, DiaSemana.MARTES, DiaSemana.MIERCOLES),
                LocalTime.of(13, 0), LocalTime.of(21, 0));
        empleados.add(new Empleado(null, "Patricia", "Gómez", "Flores", sucursal2, Especialidad.CONSULTA,
                "pass123", "patricia.gomez@ctrolpet.com", "6626666666", Puesto.EMPLEADO, horario6));

        // Empleado 7 - Hospitalización
        Horario horario7 = new Horario(Set.of(DiaSemana.JUEVES, DiaSemana.VIERNES, DiaSemana.SABADO),
                LocalTime.of(8, 0), LocalTime.of(20, 0));
        empleados.add(new Empleado(null, "Roberto", "Torres", "Vega", sucursal1, Especialidad.HOSPITALIZACION,
                "pass123", "roberto.torres@ctrolpet.com", "6627777777", Puesto.EMPLEADO, horario7));

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

        empleadosRepository.save(empleado);




        return empleadosRepository.saveAll(empleados);
    }

    private void actualizarSucursalesConEmpleados(List<Sucursal> sucursales, List<Empleado> empleados) {
        for (Sucursal sucursal : sucursales) {
            List<ObjectId> empleadosDeSucursal = empleados.stream()
                    .filter(emp -> emp.getSucursal().equals(sucursal.getIdSucursal()))
                    .map(Empleado::getIdEmpleado)
                    .toList();
            sucursal.setEmpleados(empleadosDeSucursal);
        }
        sucursalRepository.saveAll(sucursales);
    }

    private List<Dueno> seedDuenosConMascotas() {
        List<Dueno> duenos = new ArrayList<>();
        Random random = new Random();

        String[][] nombresMascotas = {
                {"Max", "Luna", "Rocky"},
                {"Bella", "Simba"},
                {"Toby", "Mia", "Zeus"},
                {"Coco"},
                {"Pelusa", "Firulais"},
                {"Whiskers", "Piolin", "Nemo"},
                {"Bobby", "Lola"},
                {"Canela"},
                {"Thor", "Loki", "Frida"},
                {"Princesa", "Duke"}
        };

        String[][] especies = {
                {"Perro", "Perro", "Perro"},
                {"Gato", "Gato"},
                {"Perro", "Gato", "Perro"},
                {"Roedor"},
                {"Perro", "Perro"},
                {"Gato", "Ave", "Roedor"},
                {"Perro", "Gato"},
                {"Gato"},
                {"Perro", "Perro", "Gato"},
                {"Perro", "Perro"}
        };

        String[][] razas = {
                {"Labrador", "Golden Retriever", "Pastor Alemán"},
                {"Siamés", "Persa"},
                {"Chihuahua", "Angora", "Bulldog"},
                {"Hámster"},
                {"Poodle", "Schnauzer"},
                {"Bengalí", "Periquito", "Cobayo"},
                {"Beagle", "Británico Pelo Corto"},
                {"Maine Coon"},
                {"Husky", "Rottweiler", "Ragdoll"},
                {"Maltés", "Boxer"}
        };

        // Dueño 1
        Direccion dir1 = new Direccion("Calle 10", "Villa Sonora", "Hermosillo", "Sonora", "83100", "234");
        List<Mascota> mascotas1 = crearMascotasConHistorial(nombresMascotas[0], especies[0], razas[0]);
        duenos.add(new Dueno(null, "Juan", "Pérez", "González", "juan.perez@email.com", "pass123",
                Instant.now().minus(35 * 365, ChronoUnit.DAYS), "6621234501", mascotas1, dir1));

        // Dueño 2
        Direccion dir2 = new Direccion("Av. Reforma", "Prados del Sur", "Hermosillo", "Sonora", "83280", "567");
        List<Mascota> mascotas2 = crearMascotasConHistorial(nombresMascotas[1], especies[1], razas[1]);
        duenos.add(new Dueno(null, "María", "López", "Martínez", "maria.lopez@email.com", "pass123",
                Instant.now().minus(28 * 365, ChronoUnit.DAYS), "6621234502", mascotas2, dir2));

        // Dueño 3
        Direccion dir3 = new Direccion("Blvd. Solidaridad", "Los Olivos", "Hermosillo", "Sonora", "83200", "890");
        List<Mascota> mascotas3 = crearMascotasConHistorial(nombresMascotas[2], especies[2], razas[2]);
        duenos.add(new Dueno(null, "Pedro", "García", "Ramírez", "pedro.garcia@email.com", "pass123",
                Instant.now().minus(42 * 365, ChronoUnit.DAYS), "6621234503", mascotas3, dir3));

        // Dueño 4
        Direccion dir4 = new Direccion("Calle Morelia", "Modelo", "Hermosillo", "Sonora", "83190", "123");
        List<Mascota> mascotas4 = crearMascotasConHistorial(nombresMascotas[3], especies[3], razas[3]);
        duenos.add(new Dueno(null, "Laura", "Hernández", "Flores", "laura.hernandez@email.com", "pass123",
                Instant.now().minus(31 * 365, ChronoUnit.DAYS), "6621234504", mascotas4, dir4));

        // Dueño 5
        Direccion dir5 = new Direccion("Calle Sonora", "Centro", "Hermosillo", "Sonora", "83000", "456");
        List<Mascota> mascotas5 = crearMascotasConHistorial(nombresMascotas[4], especies[4], razas[4]);
        duenos.add(new Dueno(null, "Carlos", "Martínez", "Soto", "carlos.martinez@email.com", "pass123",
                Instant.now().minus(38 * 365, ChronoUnit.DAYS), "6621234505", mascotas5, dir5));

        // Dueño 6
        Direccion dir6 = new Direccion("Av. Universidad", "San Benito", "Hermosillo", "Sonora", "83190", "789");
        List<Mascota> mascotas6 = crearMascotasConHistorial(nombresMascotas[5], especies[5], razas[5]);
        duenos.add(new Dueno(null, "Ana", "Rodríguez", "Vega", "ana.rodriguez@email.com", "pass123",
                Instant.now().minus(26 * 365, ChronoUnit.DAYS), "6621234506", mascotas6, dir6));

        // Dueño 7
        Direccion dir7 = new Direccion("Calle Guerrero", "Villa de Seris", "Hermosillo", "Sonora", "83280", "321");
        List<Mascota> mascotas7 = crearMascotasConHistorial(nombresMascotas[6], especies[6], razas[6]);
        duenos.add(new Dueno(null, "Roberto", "Sánchez", "Cruz", "roberto.sanchez@email.com", "pass123",
                Instant.now().minus(45 * 365, ChronoUnit.DAYS), "6621234507", mascotas7, dir7));

        // Dueño 8
        Direccion dir8 = new Direccion("Calle Nayarit", "Bachoco", "Hermosillo", "Sonora", "83180", "654");
        List<Mascota> mascotas8 = crearMascotasConHistorial(nombresMascotas[7], especies[7], razas[7]);
        duenos.add(new Dueno(null, "Sofía", "Torres", "Díaz", "sofia.torres@email.com", "pass123",
                Instant.now().minus(29 * 365, ChronoUnit.DAYS), "6621234508", mascotas8, dir8));

        // Dueño 9
        Direccion dir9 = new Direccion("Blvd. Navarrete", "Pueblitos", "Hermosillo", "Sonora", "83240", "987");
        List<Mascota> mascotas9 = crearMascotasConHistorial(nombresMascotas[8], especies[8], razas[8]);
        duenos.add(new Dueno(null, "Miguel", "Gómez", "Ruiz", "miguel.gomez@email.com", "pass123",
                Instant.now().minus(33 * 365, ChronoUnit.DAYS), "6621234509", mascotas9, dir9));

        // Dueño 10
        Direccion dir10 = new Direccion("Av. De la Cultura", "Proyecto Rio Sonora", "Hermosillo", "Sonora", "83270", "147");
        List<Mascota> mascotas10 = crearMascotasConHistorial(nombresMascotas[9], especies[9], razas[9]);
        duenos.add(new Dueno(null, "Valentina", "Ramírez", "Morales", "valentina.ramirez@email.com", "pass123",
                Instant.now().minus(27 * 365, ChronoUnit.DAYS), "6621234510", mascotas10, dir10));

        return duenoRepository.saveAll(duenos);
    }

    private List<Mascota> crearMascotasConHistorial(String[] nombres, String[] especies, String[] razas) {
        List<Mascota> mascotas = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < nombres.length; i++) {
            ObjectId idMascota = new ObjectId();
            ObjectId idHistorial = new ObjectId();

            // Crear historial clínico con medicamentos
            List<Medicamento> medicamentos = new ArrayList<>();
            medicamentos.add(new Medicamento(new ObjectId(), "Antibiótico", "10mg", "Cada 12 horas", "7 días"));
            if (random.nextBoolean()) {
                medicamentos.add(new Medicamento(new ObjectId(), "Antiparasitario", "5ml", "Dosis única", "1 día"));
            }

            HistorialClinico historial = new HistorialClinico(
                    idHistorial,
                    idMascota,
                    Instant.now().minus(random.nextInt(180), ChronoUnit.DAYS),
                    "Revisión general - Estado saludable",
                    "Mascota en buen estado, vacunas al día",
                    medicamentos
            );
            historialClinicoRepository.save(historial);

            // Crear mascota
            Mascota mascota = new Mascota(
                    idMascota,
                    nombres[i],
                    especies[i],
                    razas[i],
                    Instant.now().minus(random.nextInt(3650) + 365, ChronoUnit.DAYS), // Entre 1-10 años
                    "https://ejemplo.com/fotos/" + nombres[i].toLowerCase() + ".jpg",
                    idHistorial
            );
            mascotas.add(mascota);
        }

        return mascotas;
    }

    private List<Reserva> seedReservas(List<Dueno> duenos, List<Empleado> empleados,
                                       List<Sucursal> sucursales, List<Servicio> servicios) {
        List<Reserva> reservas = new ArrayList<>();
        Random random = new Random();
        EstadoReserva[] estados = EstadoReserva.values();

        // Crear 15 reservas de ejemplo
        for (int i = 0; i < 15; i++) {
            Dueno dueno = duenos.get(random.nextInt(duenos.size()));
            Mascota mascota = dueno.getMascotas().get(random.nextInt(dueno.getMascotas().size()));
            Empleado empleado = empleados.get(random.nextInt(empleados.size()));
            Sucursal sucursal = sucursales.get(random.nextInt(sucursales.size()));
            Servicio servicio = servicios.get(random.nextInt(servicios.size()));

            LocalDateTime fecha = LocalDateTime.now()
                    .plusDays(random.nextInt(60) - 30) // Reservas entre -30 y +30 días
                    .withHour(8 + random.nextInt(10))
                    .withMinute(0);

            reservas.add(new Reserva(
                    null,
                    empleado.getIdEmpleado(),
                    fecha,
                    estados[random.nextInt(estados.length)],
                    sucursal.getIdSucursal(),
                    dueno.getIdDueno(),
                    mascota.getIdMascota(),
                    servicio
            ));
        }

        return reservaRepository.saveAll(reservas);
    }
}
