package com.soe.jcb.eventdriven.demo.venue.domain;

import java.util.Optional;

public interface SectionRepository {

    Optional<Section> findById(Long id);
}