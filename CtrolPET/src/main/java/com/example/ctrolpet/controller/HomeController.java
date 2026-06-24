package com.example.ctrolpet.controller;

import jakarta.servlet.http.HttpSession;
import com.example.ctrolpet.model.Dueño;
import com.example.ctrolpet.model.Empleado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.ctrolpet.service.DueñoService;
import com.example.ctrolpet.service.EmpleadoService;

@Controller
public class HomeController {


    @Autowired
    private DueñoService dueñoService;

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping({"/", "/index", "/index.html"})
    public String index() {
        return "index";
    }

    @GetMapping({"/login", "/login.html"})
    public String mostrarLogin(){
        return "login";
    }

    @GetMapping("/login-admin")
    public String mostrarLoginAdmin(){
        return "login-admin";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam("correo") String correo,
                                @RequestParam("contrasenia") String contrasenia,
                                HttpSession session,
                                Model model){

        Dueño dueño = dueñoService.autenticar(correo, contrasenia);

        if(dueño != null){
            session.setAttribute("id_dueño", dueño.getId_dueño());
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

        if(empleado != null){
            session.setAttribute("id_Empleado", empleado.getId_Empleado());
            return "redirect:/admin";
        } else {
            model.addAttribute("error", "Credenciales Incorrectas");
            return "login-admin";
        }
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
