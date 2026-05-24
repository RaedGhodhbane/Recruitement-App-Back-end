package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.ContactDTO;

import java.util.List;

public interface ContactService {
    List<ContactDTO> getAllMessagesContact();

    ContactDTO sendMessageContact(ContactDTO contactDTO, Long idUserSend);
}
