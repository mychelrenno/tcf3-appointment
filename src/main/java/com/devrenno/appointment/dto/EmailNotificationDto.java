package com.devrenno.appointment.dto;

public record EmailNotificationDto(
        String to,
        String subject,
        String body
) {
}
