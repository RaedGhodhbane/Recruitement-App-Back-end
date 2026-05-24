package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.dto.AdminDTO;
import com.app.recruitmentapp.dto.ContactDTO;
import com.app.recruitmentapp.entities.ChangePassword;
import com.app.recruitmentapp.services.AdminService;
import com.app.recruitmentapp.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ContactService contactService;

    @GetMapping("/admins")
    public List<AdminDTO> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminDTO> getAdminById(@PathVariable Long id) {
        return adminService.getAdminById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<?> registerAdminWithPicture(@RequestParam Map<String, String> request,
                                                       @ModelAttribute AdminDTO adminDTO,
                                                       @RequestParam("imageFile") MultipartFile imageFile) {
        String email = request.get("email");
        String password = request.get("password");

        try {
            AdminDTO saved = adminService.registerAdminWithPicture(email, password, adminDTO, imageFile);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/updateadmin/{id}")
    public ResponseEntity<AdminDTO> updateAdmin(@PathVariable Long id, @ModelAttribute AdminDTO adminDTO, @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            AdminDTO updated = adminService.updateAdmin(id, adminDTO, imageFile);
            return ResponseEntity.ok(updated);
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

    @PutMapping("/{id}/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @PathVariable Long id,
            @RequestBody ChangePassword changePasswordRequest) {
        try {
            String message = adminService.changePassword(id, changePasswordRequest);
            return ResponseEntity.ok().body(Collections.singletonMap("message", message));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("erreur", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("erreur", e.getMessage()));
        }
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        return adminService.getFile(filename);
    }

    @GetMapping("/contact/messages")
    public List<ContactDTO> getAllMessagesContact() {
        return contactService.getAllMessagesContact();
    }
}
