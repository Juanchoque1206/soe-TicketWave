package com.soe.jcb.eventdriven.demo.user.infrastructure.persistence;

import com.soe.jcb.eventdriven.demo.user.domain.User;
import com.soe.jcb.eventdriven.demo.user.domain.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Primary adapter implementing the domain {@link UserRepository} port.
 *
 * <p>It translates between the pure domain {@link User} and the JPA entity
 * {@link UserJpaEntity}, hiding all persistence details from the application.
 */
@Component
public class UserRepositoryJpaAdapter implements UserRepository {

    private final UserJpaRepository jpaRepository;

    public UserRepositoryJpaAdapter(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public User save(User user) {
        return toDomain(jpaRepository.save(toJpa(user)));
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    private UserJpaEntity toJpa(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.getId());
        entity.setEmail(user.getEmail());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setPhone(user.getPhone());
        entity.setRole(user.getRole());
        entity.setEnabled(user.isEnabled());
        return entity;
    }

    private User toDomain(UserJpaEntity entity) {
        return new User(entity.getId(), entity.getEmail(), entity.getPasswordHash(),
                entity.getFirstName(), entity.getLastName(), entity.getPhone(),
                entity.getRole(), entity.isEnabled());
    }
}