package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.domain.common.exception.ResourceNotFoundException;
import com.ticketwave.domain.promotion.model.Promotion;
import com.ticketwave.domain.promotion.model.PromotionScope;
import com.ticketwave.domain.promotion.repository.PromotionRepository;
import com.ticketwave.infrastructure.repository.jpa.JpaVenue;
import com.ticketwave.infrastructure.repository.jpa.JpaVenueRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PromotionPersistenceAdapter implements PromotionRepository {

    private final JpaPromotionRepository jpaPromotionRepository;
    private final JpaVenueRepository jpaVenueRepository;
    private final PromotionPersistenceMapper mapper;

    public PromotionPersistenceAdapter(JpaPromotionRepository jpaPromotionRepository,
                                        JpaVenueRepository jpaVenueRepository,
                                        PromotionPersistenceMapper mapper) {
        this.jpaPromotionRepository = jpaPromotionRepository;
        this.jpaVenueRepository = jpaVenueRepository;
        this.mapper = mapper;
    }

    @Override
    public Promotion save(Promotion promotion) {
        JpaPromotion jpa = mapper.toJpa(promotion);
        if (promotion.getVenueId() != null) {
            JpaVenue venue = jpaVenueRepository.findById(promotion.getVenueId())
                    .orElseThrow(() -> new ResourceNotFoundException("Venue", "id", promotion.getVenueId()));
            jpa.setVenue(venue);
        }
        jpa = jpaPromotionRepository.save(jpa);
        return mapper.toDomain(jpa);
    }

    @Override
    public Optional<Promotion> findByCodeAndActiveTrue(String code) {
        return jpaPromotionRepository.findByCodeAndActiveTrue(code).map(mapper::toDomain);
    }

    @Override
    public List<Promotion> findByActiveTrue() {
        return jpaPromotionRepository.findByActiveTrue().stream().map(mapper::toDomain).toList();
    }

    @Override
    public int incrementCurrentUses(Long id) {
        return jpaPromotionRepository.incrementCurrentUses(id);
    }
}
