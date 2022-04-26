package com.example.scaffold.config;

import com.example.scaffold.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Configuration
public class JpaAuditingConfiguration {
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(it -> (ServletRequestAttributes) it)
                .map(it -> it.getRequest().getHeader("user"))
                .map(it -> User.parseFromJson(it).getName())
                .or(() -> Optional.of("system"));
    }
}
