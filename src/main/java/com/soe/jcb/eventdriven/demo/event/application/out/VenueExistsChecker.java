package com.soe.jcb.eventdriven.demo.event.application.out;

/**
 * Application-side port that lets the event context verify a referenced venue
 * exists without importing the venue domain (cross-context dependency inversion).
 */
public interface VenueExistsChecker {
    boolean existsById(Long venueId);
}