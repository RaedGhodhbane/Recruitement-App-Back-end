package com.app.recruitmentapp.services;

import com.app.recruitmentapp.dto.MessageDTO;
import com.app.recruitmentapp.entities.Message;
import com.app.recruitmentapp.entities.User;
import com.app.recruitmentapp.mapper.EntityMapper;
import com.app.recruitmentapp.exceptions.ResourceNotFoundException;
import com.app.recruitmentapp.repositories.MessageRepository;
import com.app.recruitmentapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService{
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Override
    public List<MessageDTO> getAllMessages() {
        return entityMapper.toMessageDTOList(messageRepository.findAll());
    }

    @Override
    public Optional<MessageDTO> getMessageById(Long id) {
        return messageRepository.findById(id).map(entityMapper::toMessageDTO);
    }

    @Override
    public MessageDTO sendMessage(MessageDTO messageDTO, Long idUserSend, Long idUserReceive) {
        Message message = entityMapper.toMessageEntity(messageDTO);
        User userSend = userRepository.findById(idUserSend).orElse(null);
        User userReceive = userRepository.findById(idUserReceive).orElse(null);
        message.setUserSend(userSend);
        message.setUserReceive(userReceive);
        return entityMapper.toMessageDTO(messageRepository.save(message));
    }

    @Override
    public void deleteMessage(Long id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Message non trouvé");
        }
    }
}
