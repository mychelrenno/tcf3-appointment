package com.devrenno.appointment.jwt;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = null;

        if (authorization != null && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
        }

        // Se não há token, retorna 401
        if (token == null || token.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Valida o token
        if (!jwtService.isValid(token)) {
            logger.warn("Token inválido ou expirado");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            // Extrai informações do token e cria a autenticação
            String email = jwtService.extractEmail(token);
            String role = jwtService.extractRole(token);
            
            // Normaliza o role para corresponder ao enum UserRole
            String normalizedRole = normalizeRole(role);
            
            logger.debug("Token válido para usuário: {} com role: {} (normalizado: {})", email, role, normalizedRole);
            
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    List.of(new SimpleGrantedAuthority(normalizedRole))
            );

            // Registra o usuario como autenticado
            SecurityContextHolder.getContext().setAuthentication(authToken);

            chain.doFilter(request, response);
        } catch (Exception e) {
            // Se houver qualquer erro ao extrair informações do token, retorna 401
            logger.error("Erro ao processar token: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    /**
     * Normaliza o role do token para corresponder ao enum UserRole
     * Converte ROLE_DOCTOR -> ROLE_DOCTOR, ROLE_NURSE -> ROLE_NURSE, ROLE_PATIENT -> ROLE_PATIENT
     */
    private String normalizeRole(String role) {
        if (role == null) {
            return "ROLE_PATIENT"; // Default
        }
        
        // Remove prefixo ROLE_ se já existir e adiciona novamente para garantir formato correto
        String roleUpper = role.toUpperCase().trim();
        if (!roleUpper.startsWith("ROLE_")) {
            roleUpper = "ROLE_" + roleUpper;
        }
        
        // Mapeia roles do user service para roles do appointment service
        if (roleUpper.contains("DOCTOR")) {
            return "ROLE_DOCTOR";
        } else if (roleUpper.contains("NURSE")) {
            return "ROLE_NURSE";
        } else if (roleUpper.contains("PATIENT")) {
            return "ROLE_PATIENT";
        }
        
        return roleUpper;
    }
}
