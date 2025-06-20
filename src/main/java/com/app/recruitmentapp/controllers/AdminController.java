package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.entities.Admin;
import com.app.recruitmentapp.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/admins")
    public List<Admin> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long id) {
        Optional<Admin> admin = adminService.getAdminById(id);
        return admin.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /*
    @PostMapping()
    public ResponseEntity<Admin> addAdmin(@RequestBody Admin admin) {
        Admin savedAdmin = adminService.saveAdmin(admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAdmin);
    }
     */

    @PostMapping("/registerAdmin")
    public ResponseEntity<?> registerAdmin(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        try {
            Admin admin = adminService.registerAdmin(email,password);
            return ResponseEntity.ok(admin);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable Long id, @RequestBody Admin admin) {
        try {
            Admin updatedadmin = adminService.updateAdmin(id, admin);
            return ResponseEntity.ok(updatedadmin);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdmin(@PathVariable Long id) {
        try {
            adminService.deleteAdmin(id);
            return ResponseEntity.ok("Admin deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/recruiters/{id}/activate")
    public ResponseEntity<Void> activateRecruiter(@PathVariable Long id) {
        adminService.activateRecruiterAccount(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/recruiters/{id}/desactivate")
    public ResponseEntity<Void> desactivateRecruiter(@PathVariable Long id) {
        adminService.desactivateRecruiterAccount(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/candidates/{id}/activate")
    public ResponseEntity<Void> activateCandidate(@PathVariable Long id) {
        adminService.activateCandidateAccount(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/candidates/{id}/desactivate")
    public ResponseEntity<Void> desactivateCandidate(@PathVariable Long id) {
        adminService.desactivateCandidateAccount(id);
        return ResponseEntity.ok().build();
    }


}

