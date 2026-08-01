package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.domain.user.model.User;
import com.ticketwave.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserPersistenceAdapter implements UserRepository {

    private final JpaUserRepository jpaUserRepository;
    private final UserPersistenceMapper mapper;

    public UserPersistenceAdapter(JpaUserRepository jpaUserRepository, UserPersistenceMapper mapper) {
        this.jpaUserRepository = jpaUserRepository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        JpaUser jpa = mapper.toJpa(user);
        jpa = jpaUserRepository.save(jpa);
        return mapper.toDomain(jpa);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaUserRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }
}
