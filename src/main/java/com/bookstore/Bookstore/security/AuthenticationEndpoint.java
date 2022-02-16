package com.bookstore.Bookstore.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bookstore.Bookstore.entities.User;
import com.bookstore.Bookstore.services.UserService;
import com.google.common.base.Function;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import io.spring.guides.gs_producing_web_service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.crypto.spec.SecretKeySpec;
import java.util.*;
import java.util.stream.Collectors;

import io.jsonwebtoken.Claims;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@Endpoint
@RequiredArgsConstructor
@Log4j2
@Service
@Component
public class AuthenticationEndpoint {
    @Autowired
    private UserService userService;
    private final PasswordEncoder passwordEncoder;
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";
    public static List<String> blackList = new ArrayList<String>();

    @CrossOrigin
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "authRequest")
    @ResponsePayload
    public AuthResponse auth(@RequestPayload AuthRequest input) {
        AuthResponse result = new AuthResponse();
        log.info("Intru in auteificare");

        // Daca am gasit utilizatorul
        User user = userService.getUserByUsername(input.getUsername());
        if (user != null) {
            log.info("Utiliz diferit de null");
            if (passwordEncoder.matches(input.getParola(),user.getPassword())) {
                log.info("Sunt la fel");
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                String accessToken = JWT.create()
                        .withIssuer("http://localhost:8080/login")
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis()+120*60*1000))
                        .withJWTId(UUID.randomUUID().toString())
                        .withClaim("role", user.getRole())
                        .sign(algorithm);
                result.setTokenValue(accessToken);
            }
            log.info("Nu am gasit utilizatorul");
            return result;
        }
        else {
            log.info("Utilizator null");
        }
        return null;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "validateRequest")
    @ResponsePayload
    public ValidateResponse validate(@RequestPayload ValidateRequest input) throws Exception {
        log.info("validate JWT...");
        ValidateResponse result = new ValidateResponse();

        /*Verific daca este in blacklist */
        if (blackList.contains(input.getTokenValue())) {
            throw new Exception("JWT is in blacklist!");
        }

        /* Prima data fac split token ului dupa . */
        String[] chunks = input.getTokenValue().split("\\.");

        /* Se desparte in payload, header, signature */
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));
        String signature = chunks[2];

        /* Pentru validare, avem nevoie de algoritmul folosit si de cheie */
        SignatureAlgorithm sa = HS256;
        SecretKeySpec secretKeySpec = new SecretKeySpec("secret".getBytes(), sa.getJcaName());

        /* JWT fara semnatura */
        String tokenWithoutSignature = chunks[0] + "." + chunks[1];

        /* Verific daca pentru header+payload, semnatura este corecta */
        log.info("Inainte de verificare");
        DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(sa, secretKeySpec);
        log.info("Verificare");
        if (!validator.isValid(tokenWithoutSignature, signature)) {
            blackList.add(input.getTokenValue());
            log.info("Could not verify JWT token integrity!");
            throw new Exception("Could not verify JWT token integrity!");
        }

        /* Extrag expiration day */
        Claims claims = Jwts.parser().setSigningKey("secret".getBytes()).parseClaimsJws(input.getTokenValue()).getBody();
        log.info("Am extras data de expirare");
        Function<Claims, Date> claimsResolver = Claims::getExpiration;
        Date expirationDate = claimsResolver.apply(claims);
        if (expirationDate.before(new Date())) {
            blackList.add(input.getTokenValue());
            log.info("JWT has expired!");
            throw new Exception("JWT has expired!");
        }

        /* Extrag sub si role */
        Function<Claims, String> claimsResolverSubject = Claims::getSubject;
        result.setSub(claimsResolverSubject.apply(claims));
        log.info("AM setat subject");

        String rol = (String) claims.get("role");
        result.setRole(rol);
        log.info("AM setat rol");

        return result;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "destroyRequest")
    @ResponsePayload
    public DestroyResponse destroy(@RequestPayload DestroyRequest input) throws Exception {
        log.info("destroy JWT...");
        DestroyResponse result = new DestroyResponse();

        /* Prima data fac split token ului dupa . */
        String[] chunks = input.getTokenValue().split("\\.");

        /* Se desparte in payload, header, signature */
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));
        String signature = chunks[2];

        /* Pentru validare, avem nevoie de algoritmul folosit si de cheie */
        SignatureAlgorithm sa = HS256;
        SecretKeySpec secretKeySpec = new SecretKeySpec("secret".getBytes(), sa.getJcaName());

        /* JWT fara semnatura */
        String tokenWithoutSignature = chunks[0] + "." + chunks[1];

        /* Verific daca pentru header+payload, semnatura este corecta */
        DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(sa, secretKeySpec);
        if (!validator.isValid(tokenWithoutSignature, signature)) {
            blackList.add(input.getTokenValue());
            log.info("Could not verify JWT token integrity!");
            throw new Exception("Could not verify JWT token integrity!");
        }

        /* Adaug in blacklist */
        blackList.add(input.getTokenValue());
        result.setMesaj("Am adaugat token ul in blacklist!");

        return result;
    }
}
