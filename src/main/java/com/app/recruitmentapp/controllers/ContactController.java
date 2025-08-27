package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.entities.Contact;
import com.app.recruitmentapp.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/contact")
public class ContactController {
    @Autowired
    private ContactService contactService;

    @GetMapping("/messages")
    public List<Contact> getAllMessagesContact() {
        return contactService.getAllMessagesContact();
    }

    @PostMapping(path = "/{idUserSend}")
    public ResponseEntity<Contact> sendMessageByUser(
            @RequestBody Contact contact, @PathVariable Long idUserSend) {
        Contact sendMessageContactByUser = contactService.sendMessageContact(contact, idUserSend);
        return ResponseEntity.status(HttpStatus.CREATED).body(sendMessageContactByUser);
    }

}
