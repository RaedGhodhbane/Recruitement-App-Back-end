package com.app.recruitmentapp.services;
import com.app.recruitmentapp.entities.Message;

import java.util.List;
import java.util.Optional;

public interface MessageService {

    List<Message> getAllMessages();

    Optional<Message> getMessageById(Long id);

    Message sendMessage(Message message, Long idUserSend, Long idUserReceive);

    void deleteMessage(Long id);

}

