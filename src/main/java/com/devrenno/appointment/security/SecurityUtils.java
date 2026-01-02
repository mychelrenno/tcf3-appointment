package com.devrenno.appointment.security;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collection;

public class SecurityUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class);
    
    /**
     * Obtém o email do usuário autenticado
     */
    public static String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            return authentication.getName(); // Retorna o email (username)
        }
        return null;
    }
    
    /**
     * Obtém a role do usuário autenticado
     */
    public static String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities() != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (!authorities.isEmpty()) {
                return authorities.iterator().next().getAuthority();
            }
        }
        return null;
    }
    
    /**
     * Verifica se o usuário tem uma role específica
     */
    public static boolean hasRole(String role) {
        String currentRole = getCurrentUserRole();
        return currentRole != null && currentRole.equals(role);
    }
    
    /**
     * Obtém o token JWT da requisição atual
     */
    public static String getCurrentToken() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
                if (authorization != null && authorization.startsWith("Bearer ")) {
                    return authorization.substring(7);
                }
            }
        } catch (Exception e) {
            logger.warn("Erro ao extrair token da requisição: {}", e.getMessage());
        }
        return null;
    }
    
    /**
     * Verifica se o usuário é paciente
     */
    public static boolean isPatient() {
        return hasRole("ROLE_PATIENT");
    }
    
    /**
     * Verifica se o usuário é médico
     */
    public static boolean isDoctor() {
        return hasRole("ROLE_DOCTOR");
    }
    
    /**
     * Verifica se o usuário é enfermeiro
     */
    public static boolean isNurse() {
        return hasRole("ROLE_NURSE");
    }
}

