package com.example.ClienteRest.security;


import com.example.ClienteRest.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * ============================================================
 *  JwtFilter — Filtro HTTP que protege los endpoints
 * ============================================================
 *
 * ¿Qué es un filtro (Filter)?
 * Un filtro intercepta CADA petición HTTP ANTES de que llegue
 * al Controller. Es como una "aduana": revisa que el request
 * tenga los papeles en regla antes de dejarlo pasar.
 *
 * Este filtro extiende OncePerRequestFilter de Spring, que
 * garantiza que el filtro se ejecuta exactamente UNA VEZ
 * por request (evita duplicados en ciertos escenarios de Spring).
 *
 * Flujo de cada petición:
 *
 *   [Cliente] → [JwtFilter] → ¿Ruta pública? → SÍ → [Controller]
 *                                              → NO → ¿Tiene token?
 *                                                      → SÍ → ¿Token válido?
 *                                                               → SÍ → [Controller]
 *                                                               → NO → 401
 *                                                      → NO → 401
 *
 * IMPORTANTE: Este filtro NO modifica ningún Controller existente.
 * Es completamente transparente para el resto del código.
 *
 * @Component → Spring registra esta clase como un bean.
 *              NexusApplication la registra como filtro activo.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    /**
     * Inyectamos el servicio JWT para poder validar tokens.
     * Spring lo inyecta automáticamente.
     */
    @Autowired
    private JwtService jwtService;

    // ================================================================
    //  Definir las rutas que NO requieren token (rutas públicas)
    // ================================================================

    /**
     * Define si una ruta es pública (no requiere autenticación).
     *
     * Rutas públicas:
     *   POST /api/auth/login  → aquí es donde SE OBTIENE el token
     *   POST /api/users       → registro de nuevos usuarios
     *   GET  /api/posts       → ver todos los posts (es público)
     *   GET  /api/posts/{id}  → ver un post (es público)
     *   GET  /api/comments/*  → ver comentarios (es público)
     *
     * @param request la petición HTTP actual
     * @return true si la ruta es pública (no necesita token)
     */
    private boolean isPublicRoute(HttpServletRequest request) {
        String method = request.getMethod();    // GET, POST, PUT, DELETE...
        String path   = request.getRequestURI(); // /api/auth/login, /api/posts, etc.

        if (method.equals("GET")) {
            if (path.equals("/") ||
                    path.startsWith("/index") ||
                    path.startsWith("/login") ||
                    path.startsWith("/registro") ||
                     path.startsWith("/reserva") ||
                    path.startsWith("/servicios") ||
                    path.startsWith("/contacto") ||
                    path.startsWith("/styles/") ||
                    path.startsWith("/js/") ||
                    path.startsWith("/imgs/")) {
                return true;
            }
        }
        if (path.startsWith("/reserva") && method.equals("POST")) return true;
        if (path.startsWith("/api/reservas/horarios") && method.equals("GET")) return true;
        if (path.startsWith("/api/servicios") && method.equals("GET")) return true;
        if (path.equals("/api/login") && method.equals("POST")) return true;
        if (path.equals("/api/registro") && method.equals("POST")) return true;
        // Las solicitudes preflight de CORS usan el método OPTIONS y no deben requerir token
        if (method.equalsIgnoreCase("OPTIONS")) return true;

        // El login siempre es público (es donde se obtiene el token)
        if (path.equals("/api/login") && method.equals("POST")) return true;

        // El registro de usuarios es público (no necesitas estar logueado para registrarte)
        if (path.equals("/api/registro") && method.equals("POST")) return true;

        // hacer una reserva sin login es publico
       if (path.equals("/api/reservas") && method.equals("POST")) return true;

        // Todo lo demás requiere autenticación
        return false;
    }

    // ================================================================
    //  Lógica principal del filtro
    // ================================================================

    /**
     * Este método se ejecuta en CADA petición HTTP.
     *
     * Parámetros que recibe Spring automáticamente:
     * @param request  el objeto que representa la petición HTTP del cliente
     * @param response el objeto que representa la respuesta que devolveremos
     * @param chain    la cadena de filtros: llamar a chain.doFilter() significa
     *                 "dejar pasar este request al siguiente filtro o al Controller"
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        // ── Paso 1: ¿Es una ruta pública? ──────────────────────────────
        if (isPublicRoute(request)) {
            // Dejamos pasar sin validar nada
            chain.doFilter(request, response);
            return; // Terminamos aquí, no seguimos con la validación
        }

        // ── Paso 2: Leer el header "Authorization" ─────────────────────
        //
        // El estándar HTTP define que los tokens JWT se envían así:
        //   Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
        //
        // "Bearer" es el esquema de autenticación (token portador).
        // Nosotros solo necesitamos la parte después de "Bearer ".
        //
        String authHeader = request.getHeader("Authorization");

        // ── Paso 3: Verificar que el header existe y tiene el formato correcto ──
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // No hay token → respondemos 401 Unauthorized
            sendUnauthorized(response, "Se requiere token de autenticación");
            return;
        }

        // ── Paso 4: Extraer el token (quitarle el prefijo "Bearer ") ───
        // "Bearer " tiene 7 caracteres, entonces substring(7) nos da solo el token
        String token = authHeader.substring(7);

        // ── Paso 5: Validar el token ────────────────────────────────────
        if (!jwtService.isTokenValid(token)) {
            // Token inválido (vencido, firma incorrecta, malformado) → 401
            sendUnauthorized(response, "Token inválido o expirado");
            return;
        }

        // ── Paso 6: Token válido → dejar pasar al Controller ───────────
        //
        // Guardamos el username (subject del token) como atributo del request.
        // Esto permite que cualquier Controller recupere el username del token
        // con: request.getAttribute("email")
        // → Se usa en GET /api/posts/mine para filtrar por el usuario autenticado.
        //
        String username = jwtService.extractEmail(token);
        request.setAttribute("email", username);

        // Continuamos la cadena: el request pasa al Controller
        chain.doFilter(request, response);
    }

    // ================================================================
    //  Método auxiliar: enviar respuesta 401
    // ================================================================

    /**
     * Escribe una respuesta 401 Unauthorized en formato JSON.
     *
     * No podemos usar ResponseEntity aquí porque estamos en un Filtro,
     * no en un Controller. Usamos el HttpServletResponse directamente
     * para escribir la respuesta.
     *
     * @param response el objeto de respuesta HTTP
     * @param message  el mensaje de error a incluir en el JSON
     */
    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // código 401
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Construimos el JSON manualmente. Para un objeto tan sencillo
        // ({ "error": "..." }) no necesitamos una librería extra.
        String json = String.format("{\"error\": \"%s\"}", message);
        response.getWriter().write(json);
    }
}
