package com.example.myapi.controller;

import com.example.myapi.model.Address;
import com.example.myapi.model.ContactInfo;
import com.example.myapi.model.User;
import com.example.myapi.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository repo;

    public UserController(UserRepository repo) { this.repo = repo; }

    @GetMapping
    public List<User> all() { return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<User> byId(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        User saved = repo.save(user);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/bulk")
    @Transactional
    public ResponseEntity<List<User>> createBulk(@RequestBody List<User> users) {
        if (users == null || users.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<User> saved = repo.saveAll(users);
        return ResponseEntity.ok(saved); // 200 with the saved users (ids filled)
    }

    // ---------- UPDATE (merge non-null fields) ----------
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User incoming) {
        return repo.findById(id).map(existing -> {
            merge(existing, incoming);
            User saved = repo.save(existing);
            return ResponseEntity.ok(saved);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ---------- DELETE by id ----------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build(); // 204
    }

    // ---- helper: merge non-null fields (nested-safe) ----
    private void merge(User target, User src) {
        if (src.getName() != null) target.setName(src.getName());
        if (src.getEmail() != null) target.setEmail(src.getEmail());
        if (src.getDateOfBirth() != null) target.setDateOfBirth(src.getDateOfBirth());
        // booleans: if you want to support toggling, decide on a sentinel; here we only update when explicitly sent
        // (If you want to always allow false/true update, switch to Boolean and check for null)
        // Example:
        // if (src.getActive() != null) target.setActive(src.getActive());

        // phoneNumbers: replace only if provided
        if (src.getPhoneNumbers() != null) target.setPhoneNumbers(src.getPhoneNumbers());

        // Address (merge by field)
        if (src.getAddress() != null) {
            if (target.getAddress() == null) target.setAddress(new Address());
            Address a = target.getAddress(), b = src.getAddress();
            if (b.getStreet() != null) a.setStreet(b.getStreet());
            if (b.getCity() != null) a.setCity(b.getCity());
            if (b.getState() != null) a.setState(b.getState());
            if (b.getZip() != null) a.setZip(b.getZip());
        }

        // ContactInfo (merge by field)
        if (src.getContactInfo() != null) {
            if (target.getContactInfo() == null) target.setContactInfo(new ContactInfo());
            ContactInfo a = target.getContactInfo(), b = src.getContactInfo();
            if (b.getSecondaryEmail() != null) a.setSecondaryEmail(b.getSecondaryEmail());
            if (b.getPreferredContactMethod() != null) a.setPreferredContactMethod(b.getPreferredContactMethod());
        }
    }
}
