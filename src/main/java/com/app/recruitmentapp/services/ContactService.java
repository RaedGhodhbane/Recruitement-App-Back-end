package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Contact;

import java.util.List;

public interface ContactService {
    List<Contact> getAllMessagesContact();

    Contact sendMessageContact(Contact contact, Long idUserSend);
}
