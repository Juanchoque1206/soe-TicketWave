package com.soe.jcb.eventdriven.demo.user.domain;

/**
 * User aggregate root.
 *
 * <p>This is a <em>pure domain</em> object: it has no Spring or JPA
 * annotations. Persistence mapping lives in the {@code infrastructure}
 * package and is applied by an adapter, so the domain model remains a plain,
 * framework-agnostic representation of the ubiquitous language.
 */
public class User {

    public enum Role {
        CUSTOMER,
        ORGANIZER,
        ADMIN
    }

    private Long id;
    private String email;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
    private boolean enabled;

    // JPA entities in infrastructure use a separate mapped class; this public
    // constructor is used by the persistence adapter to reconstruct aggregates.
    public User(Long id, String email, String passwordHash, String firstName,
                String lastName, String phone, Role role, boolean enabled) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.role = role;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public Role getRole() {
        return role;
    }

    public boolean isEnabled() {
        return enabled;
    }
}