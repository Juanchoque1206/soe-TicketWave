package com.soe.jcb.eventdriven.demo.user.application.out;

/**
 * Outbound port for credential hashing.
 *
 * <p>The application/domain never sees Spring Security's BCrypt. This port
 * lets the {code user.infrastructure.security} adapter supply the actual
 * encoder implementation while the use-case stays framework-free.
 */
public interface PasswordEncoderPort {

    String encode(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);
}