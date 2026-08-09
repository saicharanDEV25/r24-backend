package com.r24.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * One-off local utility: turns a plaintext admin password into the BCrypt
 * hash that goes into the ADMIN_PASSWORD_HASH env var. Not a Spring bean,
 * not wired into any endpoint or Render deploy — run it locally, copy the
 * printed hash, then forget it. The plaintext password itself is never
 * stored anywhere.
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
