package com.scurtis.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.scurtis.models.JwtResponse;
import com.scurtis.models.UserInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JWTUtil {
    private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expirationMs}")
    private long jwtExpirationInMs;

    public JwtResponse generateToken(UserInformation userInformation) {
        logger.info("User Details: {}", userInformation);
        var token = JWT.create()
                .withSubject(userInformation.getUserName())
                .withClaim("email", userInformation.getEmail())
                .withClaim("firstName", userInformation.getFirstName())
                .withClaim("lastName", userInformation.getLastName())
                .withIssuedAt(Date.from(Instant.now()))
                .withExpiresAt(Date.from(Instant.now().plus(jwtExpirationInMs, ChronoUnit.MILLIS)))
                .withIssuer("Badass Productions")
                .sign(Algorithm.HMAC256(secret));
        logger.info("Token: {}", token);
        return JwtResponse.builder().token(token).id(0L).build();
    }

    public UserInformation validateToken(String token) throws JWTVerificationException {
        logger.info("Validating Token: {}", token);
        var verifier = JWT.require(Algorithm.HMAC256(secret)).build();
        var jwt = verifier.verify(token);
        var userDetails = new UserInformation(jwt.getSubject(), jwt.getClaim("email").asString(), jwt.getClaim("firstName").asString(), jwt.getClaim("lastName").asString());
        logger.info("User Details: {}", userDetails);
        return userDetails;
    }
}
