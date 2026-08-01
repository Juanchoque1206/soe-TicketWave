package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.domain.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

    public User toDomain(JpaUser jpa) {
        User user = new User();
        user.setId(jpa.getId());
        user.setEmail(jpa.getEmail());
        user.setPasswordHash(jpa.getPasswordHash());
        user.setFirstName(jpa.getFirstName());
        user.setLastName(jpa.getLastName());
        user.setPhone(jpa.getPhone());
        user.setRole(jpa.getRole());
        user.setEnabled(jpa.isEnabled());
        user.setCreatedAt(jpa.getCreatedAt());
        user.setUpdatedAt(jpa.getUpdatedAt());
        return user;
    }

    public JpaUser toJpa(User domain) {
        JpaUser jpa = new JpaUser();
        jpa.setId(domain.getId());
        jpa.setEmail(domain.getEmail());
        jpa.setPasswordHash(domain.getPasswordHash());
        jpa.setFirstName(domain.getFirstName());
        jpa.setLastName(domain.getLastName());
        jpa.setPhone(domain.getPhone());
        jpa.setRole(domain.getRole());
        jpa.setEnabled(domain.isEnabled());
        return jpa;
    }
}
