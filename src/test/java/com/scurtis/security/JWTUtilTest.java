package com.scurtis.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.scurtis.models.JwtResponse;
import com.scurtis.models.UserInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JWTUtilTest {
    JWTUtil target;

    private final String secret = "123456";
    private final String username = "user";
    private final long jwtExpirationInMs = 10000;

    @BeforeEach
    void setUp() {
        target = new JWTUtil();
        ReflectionTestUtils.setField(target, "secret", secret);
        ReflectionTestUtils.setField(target, "jwtExpirationInMs", jwtExpirationInMs);
    }

    @Test
    void shouldGenerateJwtToken() {
        UserInformation userInformation = new UserInformation("userName", "email", "first", "last");

        JwtResponse result = target.generateToken(userInformation);

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
        DecodedJWT jwt = verifier.verify(result.getToken());
        assertThat(jwt.getSubject(), is(userInformation.getUserName()));
        assertThat(jwt.getClaims().size(), is(7));
        assertThat(jwt.getClaim("email").asString(), is(userInformation.getEmail()));
        assertThat(jwt.getClaim("firstName").asString(), is(userInformation.getFirstName()));
        assertThat(jwt.getClaim("lastName").asString(), is(userInformation.getLastName()));
        assertThat(jwt.getIssuer(), is("Badass Productions"));
        assertThat(jwt.getExpiresAt(), is(Date.from(jwt.getIssuedAt().toInstant().plus(jwtExpirationInMs, ChronoUnit.MILLIS))));
    }

    @Test
    void shouldValidateToken() {
        UserInformation userInformation = new UserInformation("userName", "email", "first", "last");

        UserInformation result = target.validateToken(target.generateToken(userInformation).getToken());

        assertThat(result, is(userInformation));
    }
}