package com.example.ClienteRest.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;

/**
 * ============================================================
 *  LoginResponse — DTO para la respuesta del login exitoso
 * ============================================================
 *
 * ¿Qué es este objeto?
 * Es lo que el servidor le devuelve al cliente tras un login exitoso.
 *
 * Respuesta que recibirá el cliente (código 200 OK):
 * {
 *   "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjYXJsb3NfZGV2..."
 * }
 *
 * El cliente debe guardar ese token (en localStorage, sessionStorage,
 * o una variable) y enviarlo en cada petición posterior con el header:
 *   Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
 *
 * @Data          → Lombok genera getters y setters.
 * @AllArgsConstructor → Lombok genera un constructor con todos los campos.
 *                       Así podemos hacer: new LoginResponse(token) en el controller.
 */
@Data
@AllArgsConstructor
public class LoginResponse {

    /**
     * El JWT generado para el usuario autenticado.
     *
     * Estructura interna del token (visible en jwt.io):
     * HEADER:  { "alg": "HS256" }
     * PAYLOAD: { "sub": "carlos_dev", "iat": 1..., "exp": 1... }
     * FIRMA:   HMAC-SHA256(base64(header) + "." + base64(payload), secretKey)
     *
     * El "sub" (subject) es el username del usuario.
     * "iat" es la fecha de emisión (issued at).
     * "exp" es la fecha de expiración.
     */
    private String token;
    private String idDueno;
}
