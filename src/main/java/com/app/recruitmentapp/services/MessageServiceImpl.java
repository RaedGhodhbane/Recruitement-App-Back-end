package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Message;
import com.app.recruitmentapp.entities.Recruiter;
import com.app.recruitmentapp.repositories.CandidateRepository;
import com.app.recruitmentapp.repositories.MessageRepository;
import com.app.recruitmentapp.repositories.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService{
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    @Override
    public Message sendMessageByRecruiter(Message message,Long idRecruiter) {
        Recruiter r= recruiterRepository.findById(idRecruiter).orElse(null);
        message.setRecruiter(r);
        return messageRepository.save(message);
    }

    @Override
    public Message sendMessageByCandidate(Message message, Long idCandidate) {
        Candidate c= candidateRepository.findById(idCandidate).orElse(null);
        message.setCandidate(c);
        return messageRepository.save(message);
    }

    @Override
    public Message updateMessage(Long id,Message newMessage) {
        Message m = messageRepository.findById(id).orElse(null);
        m.setFullName(newMessage.getFullName());
        m.setEmail(newMessage.getEmail());
        m.setSubject(newMessage.getSubject());
        m.setMessage(newMessage.getMessage());
        m.setRecruiter(newMessage.getRecruiter());
        m.setCandidate(newMessage.getCandidate());
        messageRepository.saveAndFlush(m);
        return m;
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
