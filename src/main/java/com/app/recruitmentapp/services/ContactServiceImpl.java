package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Admin;
import com.app.recruitmentapp.entities.Contact;
import com.app.recruitmentapp.entities.User;
import com.app.recruitmentapp.repositories.AdminRepository;
import com.app.recruitmentapp.repositories.ContactRepository;
import com.app.recruitmentapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService{
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public List<Contact> getAllMessagesContact() {
        return contactRepository.findAll();
    }

    @Override
    public Contact sendMessageContact(Contact contact, Long idUserSend) {
        User userSend = userRepository.findById(idUserSend).orElse(null);
        contact.setUserSend(userSend);
        return contactRepository.save(contact);
    }
}
