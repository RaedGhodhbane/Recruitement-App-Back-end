package com.app.recruitmentapp.services;


import com.app.recruitmentapp.entities.Message;

import java.util.List;
import java.util.Optional;

public interface MessageService {

    List<Message> getAllMessages();

    Optional<Message> getMessageById(Long id);

    Message sendMessageByRecruiter(Message message, Long idRecruiter);

    Message sendMessageByCandidate(Message message, Long idCandidate);

    Message updateMessage(Long id, Message message);

    void deleteMessage(Long id);
}
