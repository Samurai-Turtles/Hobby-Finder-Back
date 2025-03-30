package com.hobbyFinder.hubby.infra.security;

import com.hobbyFinder.hubby.models.entities.CustomPrincipal;
import com.hobbyFinder.hubby.models.entities.User;
import com.hobbyFinder.hubby.repositories.TokenBlacklistRepository;
import com.hobbyFinder.hubby.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenBlacklistRepository tokenBlacklistRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        if(token != null){

            // Verifica se o token utilizado foi revogado
            if (tokenBlacklistRepository.existsByToken(token)){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized");
                return;
            }

            var login = tokenService.validateToken(token);
            Optional<User> user =  userRepository.findById(UUID.fromString(login));

            //Como o token já é válido nesse ponto, esse optional é apenas por causa de uma função já existe no repositório.
            CustomPrincipal customPrincipal = new CustomPrincipal(user.get().getUsername(), user.get().getEmail());
            var authentication = new UsernamePasswordAuthenticationToken(customPrincipal, null, user.get().getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
