package com.example.myapi.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Core
    private String name;
    private String email;
    private LocalDate dateOfBirth;
    private boolean active = true;

    // Embedded value objects (columns will be flattened into users table)
    @Embedded
    private Address address;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "secondaryEmail", column = @Column(name = "contact_secondary_email")),
            @AttributeOverride(name = "preferredContactMethod", column = @Column(name = "contact_preferred_method"))
    })
    private ContactInfo contactInfo;

    // A user can have multiple phone numbers (stored in a separate table)
    @ElementCollection
    @CollectionTable(name = "user_phones", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "phone_number")
    private List<String> phoneNumbers;

    // Getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    public ContactInfo getContactInfo() { return contactInfo; }
    public void setContactInfo(ContactInfo contactInfo) { this.contactInfo = contactInfo; }

    public List<String> getPhoneNumbers() { return phoneNumbers; }
    public void setPhoneNumbers(List<String> phoneNumbers) { this.phoneNumbers = phoneNumbers; }
}
