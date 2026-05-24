package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.ContactDTO;
import com.app.recruitmentapp.entities.Contact;
import com.app.recruitmentapp.entities.User;
import com.app.recruitmentapp.mapper.EntityMapper;
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
    private EntityMapper entityMapper;

    @Override
    public List<ContactDTO> getAllMessagesContact() {
        return entityMapper.toContactDTOList(contactRepository.findAll());
    }

    @Override
    public ContactDTO sendMessageContact(ContactDTO contactDTO, Long idUserSend) {
        Contact contact = entityMapper.toContactEntity(contactDTO);
        User userSend = userRepository.findById(idUserSend).orElse(null);
        contact.setUserSend(userSend);
        return entityMapper.toContactDTO(contactRepository.save(contact));
    }
}
