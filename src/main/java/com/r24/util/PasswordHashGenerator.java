package com.r24.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * One-off local utility to generate the BCrypt hash for ADMIN_PASSWORD_HASH.
 * Not a Spring bean, not wired into any endpoint — run locally and copy the output.
 */
public class PasswordHashGenerator {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: pass the plaintext password as the single argument.");
            return;
        }

        System.out.println(new BCryptPasswordEncoder().encode(args[0]));
    }
}
