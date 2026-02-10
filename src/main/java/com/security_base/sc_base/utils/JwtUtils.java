package com.security_base.sc_base.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import com.auth0.jwt.interfaces.Claim;
import java.util.Map;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${security.private.key}")
    private String privateKey;
    @Value("${security.jwt.user.generator}")
    private String userGenerator;


    // Para encriptar vamos a necesitar esta clave secreta y este algoritmo
    public String createToken(Authentication authentication){
        Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

        // Esto esta dentro del security context holder
        String username = authentication.getPrincipal().toString();

        // tambien obtenemos los permisos/autorizaciones
        // la idea es traer los permisos separados por coma
        String authoritiesa = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // A partir de esto generamos el token
        String jwtToken = JWT.create()
                .withIssuer(this.userGenerator) // aca va el usuario que genera
                .withSubject(username) // a quien se le genera el token
                .withClaim("authorities",authoritiesa) // Claims con los datos
                .withIssuedAt(new Date()) // fecha de creacion
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000)) // duracion del token
                .withJWTId(UUID.randomUUID().toString()) // id al token que genera uno random
                .sign(algorithm);// mi firma
        return jwtToken;
    }

    // Metodo para decodificar
    public DecodedJWT validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(this.privateKey); // algoritmo mas clave primaria
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.userGenerator) // usuario que genero
                    .build();
            // Si todo esta OK no genera excepcion y genera el return
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT;
        }catch (JWTVerificationException exception){
            throw new JWTVerificationException("Token invalido no autorizado");
        }
    }

    // Metodo para obtener el nombre del usuario del token
    public String extractUsername (DecodedJWT decodedJWT) {
        //el subject es el usuario según establecimos al crear el token
        return decodedJWT.getSubject().toString();
    }

    //Metodos para obtener Claims

    //devuelvo un claim en particular
    public Claim getSpecificClaim (DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName);
    }

    //devuelvo todos los claims
    public Map<String, Claim> returnAllClaims (DecodedJWT decodedJWT){
        return decodedJWT.getClaims();
    }




}
