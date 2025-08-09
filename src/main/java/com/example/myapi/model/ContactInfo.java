package com.example.myapi.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class ContactInfo {
    private String secondaryEmail;
    private String preferredContactMethod; // e.g., "email" or "phone"

    // Getters & setters
    public String getSecondaryEmail() { return secondaryEmail; }
    public void setSecondaryEmail(String secondaryEmail) { this.secondaryEmail = secondaryEmail; }

    public String getPreferredContactMethod() { return preferredContactMethod; }
    public void setPreferredContactMethod(String preferredContactMethod) { this.preferredContactMethod = preferredContactMethod; }
}
