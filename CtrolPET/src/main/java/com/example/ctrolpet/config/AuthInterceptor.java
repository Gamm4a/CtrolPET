package com.example.ctrolpet.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        String uri = request.getRequestURI();


        if (session == null || (session.getAttribute("id_dueño")== null && session.getAttribute("id_Empleado")== null)){
            response.sendRedirect("/login");
            return false;
        }

        boolean esDueño = session.getAttribute("id_dueño") != null;
        boolean esEmpleado = session.getAttribute("id_Empleado") != null;

        if (esDueño  && uri.startsWith("/admin")) {
            response.sendRedirect("/perfil");
            return false;
        }

        if (esEmpleado && uri.startsWith("/perfil")) {
            response.sendRedirect("/admin");
            return false;
        }

        return true;
    }
}
