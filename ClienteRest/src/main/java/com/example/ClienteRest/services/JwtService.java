package com.example.ClienteRest.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * ============================================================
 *  JwtService — Servicio de JWT (Crear y Validar tokens)
 * ============================================================
 *
 * Esta clase tiene UNA SOLA responsabilidad: todo lo relacionado
 * con los tokens JWT. Ninguna otra clase necesita saber cómo
 * funciona JJWT internamente.
 *
 * Principio de diseño: Single Responsibility Principle (SRP).
 * → Un cambio en la librería JWT solo afecta a ESTA clase.
 *
 * ¿Qué es un JWT?
 * Un JWT es una cadena codificada en Base64 con 3 partes separadas por puntos:
 *
 *   eyJhbGciOiJIUzI1NiJ9         ← HEADER  (algoritmo de firma)
 *   .eyJzdWIiOiJjYXJsb3NfZGV2  ← PAYLOAD (datos: sub, iat, exp)
 *   .HMAC_FIRMA                   ← SIGNATURE (garantiza integridad)
 *
 * El PAYLOAD contiene los "claims" (afirmaciones):
 *   "sub"  → subject: quién es el usuario (guardamos el username)
 *   "iat"  → issued at: cuándo se generó
 *   "exp"  → expiration: cuándo expira
 *
 * La FIRMA evita que alguien modifique el token: si alguien cambia
 * cualquier carácter del payload, la firma deja de ser válida.
 *
 * @Service → Spring registra esta clase como un bean inyectable.
 */
@Service
public class JwtService {

    /**
     * @Value("${jwt.secret}") → Spring lee el valor de jwt.secret
     * del archivo application.properties y lo inyecta aquí.
     * Así no tenemos la clave "hardcodeada" en el código.
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * @Value("${jwt.expiration}") → idem, lee jwt.expiration
     * del application.properties. En milisegundos (86400000 = 24h).
     */
    @Value("${jwt.expiration}")
    private long expiration;

    // ================================================================
    //  MÉTODO PRIVADO: construir la clave de firma
    // ================================================================

    /**
     * Convierte la clave secreta (String) a un objeto SecretKey
     * que JJWT puede usar para firmar y verificar tokens.
     *
     * Keys.hmacShaKeyFor() → crea una clave HMAC-SHA256 a partir
     * de los bytes del String. HMAC-SHA256 es el algoritmo
     * más común para JWT ("alg": "HS256" en el header).
     *
     * @return la clave de firma lista para usar
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // ================================================================
    //  MÉTODO PÚBLICO 1: Generar un token
    // ================================================================

    /**
     * Genera un JWT firmado para el usuario indicado.
     *
     * Parámetros del token:
     * - subject(email) → el "sub" del token. Identifica al usuario.
     *                        Es el dato principal del JWT.
     * - issuedAt          → fecha/hora actual de generación.
     * - expiration        → fecha/hora en que vence (ahora + jwt.expiration ms).
     * - signWith          → firma el token con HMAC-SHA256 y nuestra clave secreta.
     * - compact()         → serializa todo a la cadena Base64 final.
     *
     * @param email el email de usuario que irá como "subject" del token
     * @return el JWT como String listo para enviarse al cliente
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)                                  // sub = email
                .issuedAt(new Date())                               // iat = ahora
                .expiration(new Date(System.currentTimeMillis() + expiration))  // exp = ahora + 24h
                .signWith(getSigningKey())                          // firma con HS256
                .compact();                                         // genera el String final
    }

    // ================================================================
    //  MÉTODO PÚBLICO 2: Extraer el username (subject) del token
    // ================================================================

    /**
     * Extrae el "subject" (email) del JWT.
     *
     * Para leer el token necesitamos:
     * 1. parseSignedClaims → valida la firma y parsea el token.
     *    Si la firma es inválida, lanza SignatureException.
     *    Si el token está vencido, lanza ExpiredJwtException.
     * 2. getPayload() → obtiene el cuerpo del token (los claims).
     * 3. getSubject() → obtiene el valor del campo "sub".
     *
     * Este método se usa en JwtFilter para saber qué usuario
     * corresponde al token recibido.
     *
     * @param token el JWT como String
     * @return el email guardado en el "sub" del token
     */
    public String extractEmail(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())   // indica la clave para verificar la firma
                .build()
                .parseSignedClaims(token)      // parsea y valida el token
                .getPayload();                 // obtiene el body (claims)

        return claims.getSubject();            // retorna el "sub" (username)
    }

    // ================================================================
    //  MÉTODO PÚBLICO 3: Validar si un token es válido
    // ================================================================

    /**
     * Verifica si el token JWT es válido.
     *
     * Un token es válido si:
     *   1. La FIRMA es correcta (no fue modificado).
     *   2. No está VENCIDO (la fecha "exp" es futura).
     *
     * Si cualquiera de las dos condiciones falla, JJWT lanza
     * una excepción. Usamos try-catch para convertir ese error
     * en un simple boolean: true (válido) / false (inválido).
     *
     * Los tipos de excepción más comunes:
     *   - ExpiredJwtException   → el token ya venció
     *   - SignatureException     → la firma no coincide (token manipulado)
     *   - MalformedJwtException → el token tiene formato incorrecto
     *
     * @param token el JWT a validar
     * @return true si el token es válido, false en cualquier otro caso
     */
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token); // si esto no lanza excepción, el token es válido
            return true;
        } catch (Exception e) {
            // Cualquier excepción (vencido, firma inválida, malformado) → false
            return false;
        }
    }
}
