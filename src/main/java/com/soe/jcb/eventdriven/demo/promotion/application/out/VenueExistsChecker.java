package com.soe.jcb.eventdriven.demo.promotion.application.out;

/**
 * Application-side port that lets the promotion context verify a referenced
 * venue exists without importing the venue domain (dependency inversion).
 */
public interface VenueExistsChecker {
    boolean existsById(Long venueId);
}