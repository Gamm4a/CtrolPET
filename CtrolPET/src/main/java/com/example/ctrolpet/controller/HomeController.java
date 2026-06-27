package com.example.ctrolpet.controller;

import com.example.ctrolpet.model.*;
import com.example.ctrolpet.model.Enum.DiaSemana;
import com.example.ctrolpet.model.Enum.Especialidad;
import com.example.ctrolpet.model.Enum.Puesto;
import com.example.ctrolpet.service.*;
import jakarta.servlet.http.HttpSession;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;

@Controller
public class HomeController {


    @Autowired
    private DueñoService dueñoService;

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private SucursalService sucursalService;

    @Autowired
    private ServicioService servicioService;

    @GetMapping({"/", "/index", "/index.html"})
    public String index() {
        return "index";
    }

    @GetMapping({"/login", "/login.html"})
    public String mostrarLogin(){
        return "login";
    }

    @GetMapping({"/login-admin", "/login-admin.html"})
    public String mostrarLoginAdmin(){
        return "login-admin";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam("correo") String correo,
                                @RequestParam("contrasenia") String contrasenia,
                                HttpSession session,
                                Model model){

        Dueno dueno = dueñoService.autenticar(correo, contrasenia);

        if(dueno != null){
            session.setAttribute("idDueño", dueno.getIdDueno());
            return "redirect:/index";
        } else {
            model.addAttribute("error", "Credenciales Incorrectas");
            return "login";
        }
    }

    @PostMapping("/login-admin")
    public String procesarLoginAdmin(@RequestParam("correo") String correo,
                                @RequestParam("contrasenia") String contrasenia,
                                HttpSession session,
                                Model model){

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
    public String guardarServicio(@RequestParam ("tipo") String tipo,
                                  @RequestParam("especialidad") String categoria,
                                  @RequestParam ("descripcion") String descripcion,
                                  @RequestParam ("precio") Double precio){

        Servicio servicioNuevo = new Servicio();
        servicioNuevo.setTipo(tipo);
        servicioNuevo.setDescripcion(descripcion);
        servicioNuevo.setPrecio(precio);
        servicioNuevo.setCategoria(Especialidad.valueOf(categoria));
        servicioService.guardar(servicioNuevo);

        return "redirect:/admin";
    }

    @PatchMapping("/admin/servicios/editar/{id}")
    public String editarServicio(@PathVariable("id") ObjectId id,
                                 @RequestParam ("tipo") String tipo,
                                 @RequestParam("especialidad") String categoria,
                                 @RequestParam ("descripcion") String descripcion,
                                 @RequestParam ("precio") Double precio){

        Servicio servicioEditar = new Servicio();
        servicioEditar.setTipo(tipo);
        servicioEditar.setDescripcion(descripcion);
        servicioEditar.setPrecio(precio);
        servicioEditar.setCategoria(Especialidad.valueOf(categoria));
        servicioService.actualizarParcial(id, servicioEditar);

        return "redirect:/admin";
    }

    @DeleteMapping("/admin/servicios/eliminar/{id}")
    public String eliminarServicio(@PathVariable("id") ObjectId id){
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
                                     @RequestParam("horaSalida") LocalTime horaSalida){

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
                                     @RequestParam("horaSalida") LocalTime horaSalida){

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

        if (sucursal != sucursalActual){
            sucursalService.actualizarEmpleados(sucursalActual, sucursal, empleado.getIdEmpleado());
        }

        return "redirect:/admin";
    }

    @DeleteMapping("/admin/veterinarios/eliminar/{id}")
    public String eliminarVeterinario(@PathVariable ("id") ObjectId id){
        empleadoService.eliminar(id);

        return "redirect:/admin";
    }

    @PostMapping ("/admin/sucursales/guardar")
    public String guardarSucursal(@RequestParam("nombre") String nombre,
                                  @RequestParam("telefono") String telefono,
                                  @RequestParam("calle") String calle,
                                  @RequestParam("colonia") String colonia,
                                  @RequestParam("ciudad") String ciudad,
                                  @RequestParam("estado") String estado,
                                  @RequestParam("codigoPostal") String codigoPostal,
                                  @RequestParam("numeroCasa") String numeroCasa){

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

    @PatchMapping ("/admin/sucursales/editar/{id}")
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

    @DeleteMapping ("/admin/sucursales/eliminar/{id}")
    public String eliminarSucursal(@PathVariable("id") ObjectId id){
        sucursalService.eliminar(id);

        return "redirect:/admin";
    }

    @GetMapping("/admin")
    public String adminDashboard(HttpSession session, Model model){

        ObjectId idEmpleado = (ObjectId) session.getAttribute("idEmpleado");

        if (idEmpleado == null) {
            return "redirect:/login-admin";
        }

        Empleado empleadoLogueado = empleadoService.obtenerPorId(idEmpleado);

        model.addAttribute("empleado", empleadoLogueado);
        model.addAttribute("listaReservas", reservaService.obtenerTodos());
        model.addAttribute("listaServicios", servicioService.obtenerTodos());
        model.addAttribute("listaEmpleados", empleadoService.obtenerTodos());
        model.addAttribute("listaSucursales", sucursalService.obtenerTodos());
        model.addAttribute("puestos", Puesto.values());
        model.addAttribute("especialidades", Especialidad.values());
        model.addAttribute("diasSemana", DiaSemana.values());

        return "admin";
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/servicios")
    public String servicios(){
        return "servicios";
    }

    @GetMapping("/contacto")
    public String contacto(){
        return "contacto";
    }
}
