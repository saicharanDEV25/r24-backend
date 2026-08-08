package com.r24.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;

// Emails a plain "something broke, here's where" alert whenever an
// unexpected exception reaches GlobalExceptionHandler. Throttled per
// exception+path so a repeating bug can't flood the inbox, and disabled by
// default until ALERT_MAIL_* env vars and app.alert.enabled=true are set.
@Service
public class ErrorAlertService {

    private static final long THROTTLE_MINUTES = 10;

    private final JavaMailSender mailSender;
    private final String toAddress;
    private final boolean enabled;
    private final ConcurrentHashMap<String, Instant> lastSentByKey = new ConcurrentHashMap<>();

    public ErrorAlertService(
            JavaMailSender mailSender,
            @Value("${app.alert.to}") String toAddress,
            @Value("${app.alert.enabled}") boolean enabled
    ) {
        this.mailSender = mailSender;
        this.toAddress = toAddress;
        this.enabled = enabled;
    }

    @Async
    public void alert(Exception ex, String method, String path) {
        if (!enabled) return;

        String key = ex.getClass().getName() + " " + method + " " + path;
        Instant now = Instant.now();
        Instant last = lastSentByKey.get(key);

        if (last != null && now.isBefore(last.plus(THROTTLE_MINUTES, ChronoUnit.MINUTES))) {
            return;
        }
        lastSentByKey.put(key, now);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toAddress);
            message.setSubject("[R24 Backend Alert] " + ex.getClass().getSimpleName() + " at " + method + " " + path);
            message.setText(buildBody(ex, method, path, now));
            mailSender.send(message);
        } catch (Exception mailError) {
            // Never let a failed alert email cause further problems.
            System.err.println("Failed to send error alert email: " + mailError.getMessage());
        }
    }

    private String buildBody(Exception ex, String method, String path, Instant when) {
        StringBuilder sb = new StringBuilder();
        sb.append("Time (UTC): ").append(DateTimeFormatter.ISO_INSTANT.format(when)).append("\n");
        sb.append("Request: ").append(method).append(" ").append(path).append("\n");
        sb.append("Exception: ").append(ex.getClass().getName()).append("\n");
        sb.append("Message: ").append(ex.getMessage()).append("\n\n");
        sb.append("Where (first r24 stack frames):\n");

        int shown = 0;
        for (StackTraceElement frame : ex.getStackTrace()) {
            if (frame.getClassName().startsWith("com.r24")) {
                sb.append("  at ").append(frame).append("\n");
                shown++;
                if (shown >= 8) break;
            }
        }
        if (shown == 0) {
            sb.append("  (no application-code frames — check the full logs on Render)\n");
        }

        sb.append("\nFurther repeats of this same error/endpoint are muted for ")
          .append(THROTTLE_MINUTES).append(" minutes.");

        return sb.toString();
    }
}
