package com.app.recruitmentapp.services;
import com.app.recruitmentapp.dto.MessageDTO;

import java.util.List;
import java.util.Optional;

public interface MessageService {

    List<MessageDTO> getAllMessages();

    Optional<MessageDTO> getMessageById(Long id);

    MessageDTO sendMessage(MessageDTO messageDTO, Long idUserSend, Long idUserReceive);

    void deleteMessage(Long id);

}
