package com.example.ctrolpet.controller;

import com.example.ctrolpet.model.*;
import com.example.ctrolpet.model.Enum.DiaSemana;
import com.example.ctrolpet.model.Enum.Especialidad;
import com.example.ctrolpet.model.Enum.EstadoReserva;
import com.example.ctrolpet.model.Enum.Puesto;
import com.example.ctrolpet.service.*;
import jakarta.servlet.http.HttpSession;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.*;
import java.time.format.TextStyle;
import java.util.*;

@Controller
public class HomeController {

    @Autowired
    private DuenoService duenoService;

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private SucursalService sucursalService;

    @Autowired
    private ServicioService servicioService;

    @GetMapping({ "/", "/index", "/index.html" })
    public String index() {
        return "index";
    }

    @GetMapping({ "/login", "/login.html" })
    public String mostrarLogin() {
        return "login";
    }

    @GetMapping({ "/login-admin", "/login-admin.html" })
    public String mostrarLoginAdmin() {
        return "login-admin";
    }

    @GetMapping({ "/registro", "/registro.html" })
    public String mostrarRegistro() {
        return "registro";
    }

    @PostMapping("/login-admin")
    public String procesarLoginAdmin(@RequestParam("correo") String correo,
            @RequestParam("contrasenia") String contrasenia,
            HttpSession session,
            Model model) {

        Empleado empleado = empleadoService.autenticar(correo, contrasenia);

        if (empleado != null) {
            session.setAttribute("idEmpleado", empleado.getIdEmpleado());
            return "redirect:/admin";
        } else {
            model.addAttribute("error", "Credenciales Incorrectas");
            return "login-admin";
        }

    }

    @PostMapping("/admin/servicios/guardar")
    public String guardarServicio(@RequestParam("tipo") String tipo,
            @RequestParam("especialidad") String categoria,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") Double precio,
            @RequestParam("duracion") Integer duracion) {

        Servicio servicioNuevo = new Servicio();
        servicioNuevo.setTipo(tipo);
        servicioNuevo.setDescripcion(descripcion);
        servicioNuevo.setPrecio(precio);
        servicioNuevo.setCategoria(Especialidad.valueOf(categoria));
        servicioNuevo.setDuracion(duracion);
        servicioService.guardar(servicioNuevo);

        return "redirect:/admin";
    }

    @PostMapping("/admin/servicios/subir-foto/{id}")
    public String subirFotoServicio(@PathVariable("id") ObjectId id,
            @RequestParam("file") MultipartFile file) {

        servicioService.guardarImagenesServicios(id, file);
        return "redirect:/admin";
    }

    @DeleteMapping("/admin/servicios/eliminar-foto/{id}")
    public String eliminarFotoServicio(@PathVariable("id") ObjectId id,
            @RequestParam("fotoUrl") String fotoUrl) {

        servicioService.eliminarImagenServicio(id, fotoUrl);

        return "redirect:/admin";
    }

    @PatchMapping("/admin/servicios/editar/{id}")
    public String editarServicio(@PathVariable("id") ObjectId id,
            @RequestParam("tipo") String tipo,
            @RequestParam("especialidad") String categoria,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") Double precio,
            @RequestParam("duracion") Integer duracion) {

        Servicio servicioEditar = new Servicio();
        servicioEditar.setTipo(tipo);
        servicioEditar.setDescripcion(descripcion);
        servicioEditar.setPrecio(precio);
        servicioEditar.setCategoria(Especialidad.valueOf(categoria));
        servicioEditar.setDuracion(duracion);
        servicioService.actualizarParcial(id, servicioEditar);

        return "redirect:/admin";
    }

    @DeleteMapping("/admin/servicios/eliminar/{id}")
    public String eliminarServicio(@PathVariable("id") ObjectId id) {
        servicioService.eliminar(id);

        return "redirect:/admin";
    }

    @PostMapping("/admin/veterinarios/guardar")
    public String guardarVeterinario(@RequestParam("nombre") String nombre,
            @RequestParam("apellidoPaterno") String apellidoPaterno,
            @RequestParam("apellidoMaterno") String apellidoMaterno,
            @RequestParam("sucursal") ObjectId sucursal,
            @RequestParam("especialidad") String especialidad,
            @RequestParam("telefono") String telefono,
            @RequestParam("correo") String correo,
            @RequestParam("contrasenia") String contrasenia,
            @RequestParam("puesto") String puesto,
            @RequestParam("diasSeleccionados") List<DiaSemana> diasSeleccionados,
            @RequestParam("horaEntrada") LocalTime horaEntrada,
            @RequestParam("horaSalida") LocalTime horaSalida) {

        Horario horario = new Horario();
        horario.setDias(new HashSet<>(diasSeleccionados));
        horario.setHoraEntrada(horaEntrada);
        horario.setHoraSalida(horaSalida);

        Empleado empleadoNuevo = new Empleado();
        empleadoNuevo.setNombre(nombre);
        empleadoNuevo.setApellidoPaterno(apellidoPaterno);
        empleadoNuevo.setApellidoMaterno(apellidoMaterno);
        empleadoNuevo.setEspecialidad(Especialidad.valueOf(especialidad));
        empleadoNuevo.setSucursal(sucursal);
        empleadoNuevo.setTelefono(telefono);
        empleadoNuevo.setPuesto(Puesto.valueOf(puesto));
        empleadoNuevo.setHorarios(horario);
        empleadoNuevo.setCorreo(correo);
        empleadoNuevo.setContrasenia(contrasenia);
        Empleado empleado = empleadoService.guardar(empleadoNuevo);

        sucursalService.agregarEmpleados(sucursal, empleado.getIdEmpleado());

        return "redirect:/admin";
    }

    @PatchMapping("/admin/veterinarios/editar/{id}")
    public String editarVeterinario(@PathVariable("id") ObjectId id, @RequestParam("nombre") String nombre,
            @RequestParam("apellidoPaterno") String apellidoPaterno,
            @RequestParam("apellidoMaterno") String apellidoMaterno,
            @RequestParam("sucursal") ObjectId sucursal,
            @RequestParam("especialidad") String especialidad,
            @RequestParam("telefono") String telefono,
            @RequestParam("correo") String correo,
            @RequestParam("puesto") String puesto,
            @RequestParam("diasSeleccionados") List<DiaSemana> diasSeleccionados,
            @RequestParam("horaEntrada") LocalTime horaEntrada,
            @RequestParam("horaSalida") LocalTime horaSalida) {

        Horario horario = new Horario();
        horario.setDias(new HashSet<>(diasSeleccionados));
        horario.setHoraEntrada(horaEntrada);
        horario.setHoraSalida(horaSalida);

        Empleado empleadoEditar = new Empleado();
        empleadoEditar.setNombre(nombre);
        empleadoEditar.setApellidoPaterno(apellidoPaterno);
        empleadoEditar.setApellidoMaterno(apellidoMaterno);
        empleadoEditar.setEspecialidad(Especialidad.valueOf(especialidad));
        empleadoEditar.setSucursal(sucursal);
        empleadoEditar.setTelefono(telefono);
        empleadoEditar.setPuesto(Puesto.valueOf(puesto));
        empleadoEditar.setHorarios(horario);
        empleadoEditar.setCorreo(correo);
        Empleado empleado = empleadoService.actualizarParcial(id, empleadoEditar);

        ObjectId sucursalActual = empleadoService.obtenerPorId(id).getSucursal();

        if (sucursal != sucursalActual) {
            sucursalService.actualizarEmpleados(sucursalActual, sucursal, empleado.getIdEmpleado());
        }

        return "redirect:/admin";
    }

    @DeleteMapping("/admin/veterinarios/eliminar/{id}")
    public String eliminarVeterinario(@PathVariable("id") ObjectId id) {
        empleadoService.eliminar(id);

        return "redirect:/admin";
    }

    @PostMapping("/admin/sucursales/guardar")
    public String guardarSucursal(@RequestParam("nombre") String nombre,
            @RequestParam("telefono") String telefono,
            @RequestParam("calle") String calle,
            @RequestParam("colonia") String colonia,
            @RequestParam("ciudad") String ciudad,
            @RequestParam("estado") String estado,
            @RequestParam("codigoPostal") String codigoPostal,
            @RequestParam("numeroCasa") String numeroCasa) {

        Direccion direccion = new Direccion();
        direccion.setCalle(calle);
        direccion.setColonia(colonia);
        direccion.setCiudad(ciudad);
        direccion.setEstado(estado);
        direccion.setCodigoPostal(codigoPostal);
        direccion.setNumeroCasa(numeroCasa);

        Sucursal nuevaSucursal = new Sucursal();
        nuevaSucursal.setNombre(nombre);
        nuevaSucursal.setTelefono(telefono);
        nuevaSucursal.setDireccion(direccion);

        sucursalService.guardar(nuevaSucursal);

        return "redirect:/admin";
    }

    @PatchMapping("/admin/sucursales/editar/{id}")
    public String editarSucursal(@PathVariable("id") ObjectId id, @RequestParam("nombre") String nombre,
            @RequestParam("telefono") String telefono,
            @RequestParam("calle") String calle,
            @RequestParam("colonia") String colonia,
            @RequestParam("ciudad") String ciudad,
            @RequestParam("estado") String estado,
            @RequestParam("codigoPostal") String codigoPostal,
            @RequestParam("numeroCasa") String numeroCasa) {

        Direccion direccion = new Direccion();
        direccion.setCalle(calle);
        direccion.setColonia(colonia);
        direccion.setCiudad(ciudad);
        direccion.setEstado(estado);
        direccion.setCodigoPostal(codigoPostal);
        direccion.setNumeroCasa(numeroCasa);

        Sucursal sucursalEditar = new Sucursal();
        sucursalEditar.setNombre(nombre);
        sucursalEditar.setTelefono(telefono);
        sucursalEditar.setDireccion(direccion);

        sucursalService.actualizarParcial(id, sucursalEditar);

        return "redirect:/admin";
    }

    @DeleteMapping("/admin/sucursales/eliminar/{id}")
    public String eliminarSucursal(@PathVariable("id") ObjectId id) {
        sucursalService.eliminar(id);

        return "redirect:/admin";
    }

    @GetMapping("/admin")
    public String adminDashboard(HttpSession session, Model model,
            @RequestParam(value = "buscar", required = false) String buscar,
            @RequestParam(value = "desde", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(value = "hasta", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        ObjectId idEmpleado = (ObjectId) session.getAttribute("idEmpleado");

        if (idEmpleado == null) {
            return "redirect:/login-admin";
        }

        List<Reserva> todasLasReservas = (buscar != null && !buscar.isBlank())
                ? reservaService.buscar(buscar, duenoService, empleadoService)
                : reservaService.obtenerTodos();

        if (desde != null || hasta != null) {
            todasLasReservas = reservaService.filtrarPorFecha(todasLasReservas, desde, hasta);
        }

        Empleado empleadoLogueado = empleadoService.obtenerPorId(idEmpleado);

        model.addAttribute("buscar", buscar);
        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);
        model.addAttribute("empleado", empleadoLogueado);
        model.addAttribute("listaReservas", todasLasReservas);
        model.addAttribute("listaServicios", servicioService.obtenerTodos());
        model.addAttribute("listaEmpleados", empleadoService.obtenerTodos());
        model.addAttribute("listaSucursales", sucursalService.obtenerTodos());
        model.addAttribute("listaDuenos", duenoService.obtenerTodos());
        model.addAttribute("puestos", Puesto.values());
        model.addAttribute("especialidades", Especialidad.values());
        model.addAttribute("diasSemana", DiaSemana.values());

        return "admin";
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/servicios")
    public String servicios() {
        return "servicios";
    }

    @GetMapping("/contacto")
    public String contacto() {
        return "contacto";
    }

    @GetMapping("/reserva")
    public String reservaSinLogin(Model model,HttpSession session) {
        model.addAttribute("listaServicios", servicioService.obtenerTodos());
        model.addAttribute("listaSucursales", sucursalService.obtenerTodos());
        model.addAttribute("listaVeterinarios", empleadoService.obtenerTodos());
        ObjectId idDueno = (ObjectId) session.getAttribute("idDueño");
        if (idDueno != null) {
            Dueno dueno = duenoService.obtenerPorId(idDueno);
            model.addAttribute("dueno", dueno);
            model.addAttribute("listaMascotas", dueno.getMascotas());
        }
        return "reserva";
    }

    @PostMapping("/reserva/dueno")
    public String reservaConLogin(@RequestParam("idDueno") ObjectId idDueno, Model model, HttpSession session) {

        ObjectId idEmpleado = (ObjectId) session.getAttribute("idEmpleado");

        if (idEmpleado == null) {
            return "redirect:/login";
        }

        Dueno dueno = duenoService.obtenerPorId(idDueno);
        Empleado empleadoLogueado = empleadoService.obtenerPorId(idEmpleado);
        model.addAttribute("dueno", dueno);
        model.addAttribute("empleado", empleadoLogueado);
        model.addAttribute("listaMascotas", dueno.getMascotas());
        model.addAttribute("listaServicios", servicioService.obtenerTodos());
        model.addAttribute("listaVeterinarios", empleadoService.obtenerTodos());
        model.addAttribute("listaSucursales", sucursalService.obtenerTodos());

        return "reserva";
    }

    @PostMapping("/reserva/filtrar-horarios")
    public String filtrarHorarios(@RequestParam("idDueno") ObjectId idDueno,
            @RequestParam("idMascota") ObjectId idMascota,
            @RequestParam("idSucursal") ObjectId idSucursal,
            @RequestParam("idServicio") ObjectId idServicio,
            @RequestParam("fecha") String fechaString,
            Model model, HttpSession session) {

        ObjectId idEmpleado = (ObjectId) session.getAttribute("idEmpleado");

        if (idEmpleado == null) {
            return "redirect:/login";
        }

        Dueno dueno = duenoService.obtenerPorId(idDueno);
        Servicio servicio = servicioService.obtenerPorId(idServicio);

        LocalDate fecha = LocalDate.parse(fechaString);
        DayOfWeek diaIngles = fecha.getDayOfWeek();
        String diaEspanol = diaIngles.getDisplayName(TextStyle.FULL, new Locale("es", "MX")).toUpperCase();
        diaEspanol = java.text.Normalizer.normalize(diaEspanol, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        String especialidadRequerida = servicio.getCategoria().name();
        List<Empleado> vetsDisponibles = empleadoService.empleadoDisponible(especialidadRequerida, diaEspanol);
        Map<LocalTime, Empleado> mapaHorarios = new TreeMap<>();

        for (Empleado emp : vetsDisponibles) {
            if (emp.getSucursal() != null && emp.getSucursal().equals(idSucursal)) {
                List<Reserva> citasDelDia = reservaService.obtenerPorEmpleadoYFecha(emp.getIdEmpleado(), fecha);
                List<LocalTime> rangosDelEmpleado = servicioService.rangoCitas(
                        emp.getHorarios().getHoraEntrada(),
                        emp.getHorarios().getHoraSalida(),
                        servicio.getDuracion());

                for (LocalTime hora : rangosDelEmpleado) {
                    if (!reservaService.horarioOcupado(hora, servicio.getDuracion(), citasDelDia)) {
                        mapaHorarios.put(hora, emp);
                    }
                }
            }
        }

        model.addAttribute("mapaHorarios", mapaHorarios);
        model.addAttribute("dueno", dueno);
        Empleado empleadoLogueado = empleadoService.obtenerPorId(idEmpleado);
        model.addAttribute("empleado", empleadoLogueado);
        model.addAttribute("listaMascotas", dueno.getMascotas());
        model.addAttribute("listaServicios", servicioService.obtenerTodos());
        model.addAttribute("listaSucursales", sucursalService.obtenerTodos());
        model.addAttribute("mascotaSeleccionada", idMascota);
        model.addAttribute("sucursalSeleccionada", idSucursal);
        model.addAttribute("servicioSeleccionado", idServicio);
        model.addAttribute("fechaSeleccionada", fechaString);

        return "reserva";
    }

    @PostMapping("/reserva/guardar")
    public String guardarCita(@RequestParam("idDueno") ObjectId idDueno,
            @RequestParam("idMascota") ObjectId idMascota,
            @RequestParam("idSucursal") ObjectId idSucursal,
            @RequestParam("idServicio") ObjectId idServicio,
            @RequestParam("fecha") String fechaString,
            @RequestParam("horaCombinada") String horaCombinada,
            HttpSession session) {

        ObjectId idEmpleado = (ObjectId) session.getAttribute("idEmpleado");

        String[] partes = horaCombinada.split("_");
        ObjectId idVeterinarioElegido = new ObjectId(partes[0]);
        String horaString = partes[1].trim();
        System.out.println(horaString);
        LocalDate fecha = LocalDate.parse(fechaString);

        LocalTime hora = LocalTime.parse(horaString);

        LocalDateTime horayFecha = LocalDateTime.of(fecha, hora);

        Reserva reserva = new Reserva();
        reserva.setMascota(idMascota);
        reserva.setFecha(horayFecha);
        reserva.setServicios(servicioService.obtenerPorId(idServicio));
        reserva.setIdSucursal(idSucursal);
        reserva.setIdEmpleado(idVeterinarioElegido);
        reserva.setDueno(idDueno);
        reservaService.guardar(reserva);

        if (idEmpleado != null) {
            return "redirect:/admin";
        }
        return "redirect:/";
    }

    @PostMapping("/reserva/sin-login")
    public String crearReservaSinLogin(@RequestParam("nombre") String nombre,
            @RequestParam("telefono") String telefono,
            @RequestParam("nombreMascota") String nombreMascota,
            @RequestParam("especieMascota") String especieMascota,
            @RequestParam("razaMascota") String razaMascota,
            @RequestParam("idSucursal") ObjectId idSucursal,
            @RequestParam("idServicio") ObjectId idServicio,
            @RequestParam("fecha") String fechaString,
            Model model) {

        Mascota mascota = new Mascota();
        mascota.setIdMascota(new ObjectId());
        mascota.setNombre(nombreMascota);
        mascota.setEspecie(especieMascota);
        mascota.setRaza(razaMascota);

        Dueno dueno = new Dueno();
        dueno.setNombre(nombre);
        dueno.setTelefono(telefono);
        dueno.setMascotas(new ArrayList<>(List.of(mascota)));

        dueno = duenoService.guardar(dueno);

        Servicio servicio = servicioService.obtenerPorId(idServicio);

        LocalDate fecha = LocalDate.parse(fechaString);
        DayOfWeek diaIngles = fecha.getDayOfWeek();
        String diaEspañol = diaIngles.getDisplayName(TextStyle.FULL, new Locale("es", "MX")).toUpperCase();
        diaEspañol = java.text.Normalizer.normalize(diaEspañol, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        String especialidadRequerida = servicio.getCategoria().name();
        List<Empleado> vetsDisponibles = empleadoService.empleadoDisponible(especialidadRequerida, diaEspañol);
        Map<LocalTime, Empleado> mapaHorarios = new TreeMap<>();

        for (Empleado emp : vetsDisponibles) {
            if (emp.getSucursal() != null && emp.getSucursal().equals(idSucursal)) {
                List<LocalTime> rangosDelEmpleado = servicioService.rangoCitas(
                        emp.getHorarios().getHoraEntrada(), emp.getHorarios().getHoraSalida(), servicio.getDuracion());

                for (LocalTime hora : rangosDelEmpleado) {
                    mapaHorarios.put(hora, emp);
                }
            }
        }
        model.addAttribute("mapaHorarios", mapaHorarios);
        model.addAttribute("dueno", dueno);
        model.addAttribute("listaMascotas", dueno.getMascotas());
        model.addAttribute("listaServicios", servicioService.obtenerTodos());
        model.addAttribute("listaSucursales", sucursalService.obtenerTodos());
        model.addAttribute("mascotaSeleccionada", mascota.getIdMascota());
        model.addAttribute("sucursalSeleccionada", idSucursal);
        model.addAttribute("servicioSeleccionado", idServicio);
        model.addAttribute("fechaSeleccionada", fechaString);
        model.addAttribute("esInvitado", true);

        return "reserva";

    }

    @GetMapping("/admin/duenos")
    public String verTablaDuenos(HttpSession session, Model model) {
        ObjectId idEmpleado = (ObjectId) session.getAttribute("idEmpleado");
        if (idEmpleado == null) {
            return "redirect:/login-admin";
        }
        Empleado empleadoLogueado = empleadoService.obtenerPorId(idEmpleado);
        model.addAttribute("empleado", empleadoLogueado);
        model.addAttribute("listaDuenos", duenoService.obtenerTodos());
        return "admin-duenos";
    }

    @GetMapping("/admin/duenos/editar/{id}")
    public String editarDueno(@PathVariable("id") ObjectId idDueno,
            HttpSession session,
            Model model,
            @RequestParam(value = "editar", required = false) Boolean editar) {
        ObjectId idEmpleado = (ObjectId) session.getAttribute("idEmpleado");
        if (idEmpleado == null) {
            return "redirect:/login-admin";
        }
        Empleado empleadoLogueado = empleadoService.obtenerPorId(idEmpleado);
        Dueno dueno = duenoService.obtenerPorId(idDueno);
        model.addAttribute("empleado", empleadoLogueado);
        model.addAttribute("dueno", dueno);
        model.addAttribute("mascotas", dueno.getMascotas());
        model.addAttribute("modoEdicion", editar != null && editar);

        return "duenos";
    }

    @PostMapping("/admin/duenos/actualizar/{id}")
    public String actualizarDueno(@PathVariable("id") ObjectId idDueno,
            @RequestParam("nombre") String nombre,
            @RequestParam("apellidoPaterno") String apellidoPaterno,
            @RequestParam("apellidoMaterno") String apellidoMaterno,
            @RequestParam("telefono") String telefono,
            @RequestParam("correo") String correo,
            @RequestParam("fechaNacimiento") LocalDate fechaNacimiento,
            HttpSession session) {

        ObjectId idEmpleado = (ObjectId) session.getAttribute("idEmpleado");
        if (idEmpleado == null) {
            return "redirect:/login-admin";
        }

        duenoService.actualizarDatosDueno(idDueno, nombre, apellidoPaterno, apellidoMaterno, telefono, correo,
                fechaNacimiento);

        return "redirect:/admin/duenos/editar/" + idDueno.toHexString();
    }

    @PostMapping("/admin/mascotas/cambiar-foto/{id}")
    public String actualizarFotoMascota(@PathVariable("id") ObjectId idMascota,
            @RequestParam("fotoMascota") MultipartFile file,
            @RequestParam("idDueno") ObjectId idDueno,
            HttpSession session) {

        ObjectId idEmpleado = (ObjectId) session.getAttribute("idEmpleado");
        if (idEmpleado == null) {
            return "redirect:/login-admin";
        }

        if (!file.isEmpty()) {
            Mascota mascota = duenoService.getMascotaById(idMascota, idDueno);
            if (mascota != null) {
                duenoService.guardarFotoMascota(idDueno, idMascota, file);
            }
        }

        return "redirect:/admin/duenos/editar/" + idDueno.toHexString();
    }

    @GetMapping("/admin/mascotas")
    public String verTablaMascotas(HttpSession session, Model model) {
        ObjectId idEmpleado = (ObjectId) session.getAttribute("idEmpleado");
        if (idEmpleado == null) {
            return "redirect:/login-admin";
        }
        Empleado empleadoLogueado = empleadoService.obtenerPorId(idEmpleado);
        model.addAttribute("empleado", empleadoLogueado);
        model.addAttribute("listaDuenos", duenoService.obtenerTodos());
        return "admin-mascotas";
    }

    @GetMapping("/admin/mascotas/editar/{id}")
    public String editarMascota(@PathVariable("id") ObjectId idMascota,
            @RequestParam("idDueno") ObjectId idDueno,
            HttpSession session, Model model,
            @RequestParam(value = "editar", required = false) Boolean editar) {
        ObjectId idEmpleado = (ObjectId) session.getAttribute("idEmpleado");
        if (idEmpleado == null) {
            return "redirect:/login-admin";
        }

        Dueno dueno = duenoService.obtenerPorId(idDueno);
        Empleado empleadoLogueado = empleadoService.obtenerPorId(idEmpleado);
        Mascota mascota = duenoService.getMascotaById(idMascota, idDueno);
        model.addAttribute("empleado", empleadoLogueado);
        model.addAttribute("mascota", mascota);
        model.addAttribute("dueno", dueno);
        model.addAttribute("modoEdicion", editar != null && editar);

        return "mascotas";
    }

    @PostMapping("/admin/mascotas/actualizar/{id}")
    public String actualizarMascota(@PathVariable("id") ObjectId idMascota,
            @RequestParam("idDueno") ObjectId idDueno,
            @RequestParam("nombre") String nombre,
            @RequestParam("especie") String especie,
            @RequestParam("raza") String raza,
            @RequestParam("fechaNacimiento") LocalDate fechaNacimiento,
            HttpSession session) {

        ObjectId idEmpleado = (ObjectId) session.getAttribute("idEmpleado");
        if (idEmpleado == null) {
            return "redirect:/login-admin";
        }

        duenoService.actualizarDatosMascota(idDueno, idMascota, nombre, especie, raza, fechaNacimiento);
        return "redirect:/admin/mascotas/editar/" + idMascota.toHexString() + "?idDueno=" + idDueno.toHexString();
    }

    @DeleteMapping("/admin/mascotas/eliminar/{id}")
    public String eliminarMascotaDueno(@PathVariable("id") ObjectId idMascota,
            @RequestParam("idDueno") ObjectId idDueno,
            HttpSession session, Model model) {
        ObjectId idEmpleado = (ObjectId) session.getAttribute("idEmpleado");
        if (idEmpleado == null) {
            return "redirect:/login-admin";
        }

        Empleado empleadoLogueado = empleadoService.obtenerPorId(idEmpleado);
        duenoService.eliminarMascota(idDueno, idMascota);

        return "redirect:/admin";
    }

    @DeleteMapping("/admin/duenos/eliminar/{id}")
    public String eliminarDueno(@PathVariable("id") ObjectId idDueno,
            HttpSession session, Model model) {
        ObjectId idEmpleado = (ObjectId) session.getAttribute("idEmpleado");
        if (idEmpleado == null) {
            return "redirect:/login-admin";
        }

        Empleado empleadoLogueado = empleadoService.obtenerPorId(idEmpleado);
        duenoService.eliminarDueno(idDueno);

        return "redirect:/admin";
    }

    @GetMapping("/admin/citas")
    public String verTablaCitas(HttpSession session, Model model,
            @RequestParam(value = "buscar", required = false) String buscar,
            @RequestParam(value = "desde", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(value = "hasta", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta)

    {
        ObjectId idEmpleado = (ObjectId) session.getAttribute("idEmpleado");
        if (idEmpleado == null) {
            return "redirect:/login-admin";
        }

        List<Reserva> listaReservas = (buscar != null && !buscar.isBlank())
                ? reservaService.buscar(buscar, duenoService, empleadoService)
                : reservaService.obtenerTodos();

        if (desde != null || hasta != null) {
            listaReservas = reservaService.filtrarPorFecha(listaReservas, desde, hasta);
        }

        Empleado empleadoLogueado = empleadoService.obtenerPorId(idEmpleado);
        model.addAttribute("empleado", empleadoLogueado);
        model.addAttribute("buscar", buscar);
        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);
        model.addAttribute("listaReservas", listaReservas);
        return "admin-citas";
    }

    @DeleteMapping("/admin/citas/eliminar/{id}")
    public String eliminarCita(@PathVariable String id, HttpSession session) {
        ObjectId idEmpleado = (ObjectId) session.getAttribute("idEmpleado");
        if (idEmpleado == null) {
            return "redirect:/login-admin";
        }
        reservaService.eliminar(new ObjectId(id));
        return "redirect:/admin/citas";
    }

    @GetMapping("/admin/citas/editar/{id}")
    public String editarCita(@PathVariable String id, HttpSession session, Model model) {
        ObjectId idEmpleado = (ObjectId) session.getAttribute("idEmpleado");
        if (idEmpleado == null) {
            return "redirect:/login-admin";
        }
        Empleado empleadoLogueado = empleadoService.obtenerPorId(idEmpleado);
        Reserva reserva = reservaService.obtenerPorId(new ObjectId(id));

        model.addAttribute("empleado", empleadoLogueado);
        model.addAttribute("reserva", reserva);
        model.addAttribute("listaServicios", servicioService.obtenerTodos());
        model.addAttribute("listaEmpleados", empleadoService.obtenerTodos());
        model.addAttribute("listaSucursales", sucursalService.obtenerTodos());

        return "admin-editar-cita";
    }

    @PatchMapping("/admin/citas/editar/{id}")
    public String actualizarCita(@PathVariable String id,
            @RequestParam ObjectId idServicio,
            @RequestParam ObjectId idEmpleado,
            @RequestParam ObjectId idSucursal,
            @RequestParam String estado,
            HttpSession session) {
        ObjectId idEmpleadoSession = (ObjectId) session.getAttribute("idEmpleado");
        if (idEmpleadoSession == null) {
            return "redirect:/login-admin";
        }

        Reserva reserva = reservaService.obtenerPorId(new ObjectId(id));
        reserva.setServicios(servicioService.obtenerPorId(idServicio));
        reserva.setIdEmpleado(idEmpleado);
        reserva.setIdSucursal(idSucursal);
        reserva.setEstado(EstadoReserva.valueOf(estado));

        reservaService.guardar(reserva);
        return "redirect:/admin/citas";
    }
}
