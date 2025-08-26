package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Message;
import com.app.recruitmentapp.entities.Recruiter;
import com.app.recruitmentapp.entities.User;
import com.app.recruitmentapp.repositories.CandidateRepository;
import com.app.recruitmentapp.repositories.MessageRepository;
import com.app.recruitmentapp.repositories.RecruiterRepository;
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

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    @Override
    public Message sendMessage(Message message,Long idUserSend, Long idUserReceive) {
        User userSend = userRepository.findById(idUserSend).orElse(null);
        User userReceive = userRepository.findById(idUserReceive).orElse(null);
        message.setUserSend(userSend);
        message.setUserReceive(userReceive);
        return messageRepository.save(message);
    }

    @Override
    public void deleteMessage(Long id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
        } else {
            throw new RuntimeException("Message non trouvé");
        }
    }
}
