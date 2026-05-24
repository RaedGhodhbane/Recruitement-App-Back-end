package com.app.recruitmentapp.mapper;

import com.app.recruitmentapp.dto.*;
import com.app.recruitmentapp.entities.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityMapper {

    public UserDTO toUserDTO(User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setFirstName(user.getFirstName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole() != null ? user.getRole().name() : null);
        return dto;
    }

    public AdminDTO toAdminDTO(Admin admin) {
        if (admin == null) return null;
        AdminDTO dto = new AdminDTO();
        dto.setId(admin.getId());
        dto.setName(admin.getName());
        dto.setFirstName(admin.getFirstName());
        dto.setEmail(admin.getEmail());
        dto.setRole(admin.getRole() != null ? admin.getRole().name() : null);
        dto.setActive(admin.getActive());
        dto.setGender(admin.getGender());
        dto.setBirthdate(admin.getBirthdate());
        dto.setAddress(admin.getAddress());
        dto.setCity(admin.getCity());
        dto.setState(admin.getState());
        dto.setCountry(admin.getCountry());
        dto.setPhone(admin.getPhone());
        dto.setImage(admin.getImage());
        return dto;
    }

    public CandidateDTO toCandidateDTO(Candidate candidate) {
        if (candidate == null) return null;
        CandidateDTO dto = new CandidateDTO();
        dto.setId(candidate.getId());
        dto.setName(candidate.getName());
        dto.setFirstName(candidate.getFirstName());
        dto.setEmail(candidate.getEmail());
        dto.setRole(candidate.getRole() != null ? candidate.getRole().name() : null);
        dto.setCv(candidate.getCv());
        dto.setCvPath(candidate.getCvPath());
        dto.setDescription(candidate.getDescription());
        dto.setAddress(candidate.getAddress());
        dto.setTitle(candidate.getTitle());
        dto.setImage(candidate.getImage());
        dto.setPhone(candidate.getPhone());
        dto.setDateOfBirth(candidate.getDateOfBirth());
        dto.setGender(candidate.getGender());
        dto.setActive(candidate.getActive());
        return dto;
    }

    public RecruiterDTO toRecruiterDTO(Recruiter recruiter) {
        if (recruiter == null) return null;
        RecruiterDTO dto = new RecruiterDTO();
        dto.setId(recruiter.getId());
        dto.setName(recruiter.getName());
        dto.setFirstName(recruiter.getFirstName());
        dto.setEmail(recruiter.getEmail());
        dto.setRole(recruiter.getRole() != null ? recruiter.getRole().name() : null);
        dto.setAddress(recruiter.getAddress());
        dto.setCreationDate(recruiter.getCreationDate());
        dto.setImage(recruiter.getImage());
        dto.setActive(recruiter.getActive());
        dto.setCompanyName(recruiter.getCompanyName());
        dto.setPhone(recruiter.getPhone());
        dto.setWebsite(recruiter.getWebsite());
        dto.setDescription(recruiter.getDescription());
        return dto;
    }

    public OfferDTO toOfferDTO(Offer offer) {
        if (offer == null) return null;
        OfferDTO dto = new OfferDTO();
        dto.setId(offer.getId());
        dto.setTitle(offer.getTitle());
        dto.setDescription(offer.getDescription());
        dto.setType(offer.getType());
        dto.setAddress(offer.getAddress());
        dto.setSalary(offer.getSalary());
        dto.setExperience(offer.getExperience());
        dto.setPublicationDate(offer.getPublicationDate());
        dto.setExpirationDate(offer.getExpirationDate());
        dto.setRecruiterId(offer.getRecruiter() != null ? offer.getRecruiter().getId() : null);
        return dto;
    }

    public CandidacyDTO toCandidacyDTO(Candidacy candidacy) {
        if (candidacy == null) return null;
        CandidacyDTO dto = new CandidacyDTO();
        dto.setId(candidacy.getId());
        dto.setSubmissionDate(candidacy.getSubmissionDate());
        dto.setStatus(candidacy.getStatus() != null ? candidacy.getStatus().name() : null);
        dto.setScore(candidacy.getScore());
        dto.setCandidateId(candidacy.getCandidate() != null ? candidacy.getCandidate().getId() : null);
        dto.setOfferId(candidacy.getOffer() != null ? candidacy.getOffer().getId() : null);
        return dto;
    }

    public SkillDTO toSkillDTO(Skill skill) {
        if (skill == null) return null;
        SkillDTO dto = new SkillDTO();
        dto.setId(skill.getId());
        dto.setTitle(skill.getTitle());
        dto.setPercentage(skill.getPercentage());
        dto.setCandidateId(skill.getCandidate() != null ? skill.getCandidate().getId() : null);
        return dto;
    }

    public ExperienceDTO toExperienceDTO(Experience experience) {
        if (experience == null) return null;
        ExperienceDTO dto = new ExperienceDTO();
        dto.setId(experience.getId());
        dto.setCompanyName(experience.getCompanyName());
        dto.setJobTitle(experience.getJobTitle());
        dto.setStartExpDate(experience.getStartExpDate());
        dto.setEndExpDate(experience.getEndExpDate());
        dto.setDescription(experience.getDescription());
        dto.setCandidateId(experience.getCandidate() != null ? experience.getCandidate().getId() : null);
        return dto;
    }

    public EducationDTO toEducationDTO(Education education) {
        if (education == null) return null;
        EducationDTO dto = new EducationDTO();
        dto.setId(education.getId());
        dto.setDiploma(education.getDiploma());
        dto.setUniversity(education.getUniversity());
        dto.setEndDate(education.getEndDate());
        dto.setDescription(education.getDescription());
        dto.setCandidateId(education.getCandidate() != null ? education.getCandidate().getId() : null);
        return dto;
    }

    public QuestionDTO toQuestionDTO(Question question) {
        if (question == null) return null;
        QuestionDTO dto = new QuestionDTO();
        dto.setId(question.getId());
        dto.setTitle(question.getTitle());
        dto.setChoice1(question.getChoice1());
        dto.setChoice2(question.getChoice2());
        dto.setChoice3(question.getChoice3());
        dto.setResponse(question.getResponse());
        dto.setOfferId(question.getOffer() != null ? question.getOffer().getId() : null);
        return dto;
    }

    public FavouriteDTO toFavouriteDTO(Favourite favourite) {
        if (favourite == null) return null;
        FavouriteDTO dto = new FavouriteDTO();
        dto.setId(favourite.getId());
        dto.setUserId(favourite.getUser() != null ? favourite.getUser().getId() : null);
        dto.setOfferId(favourite.getOffer() != null ? favourite.getOffer().getId() : null);
        return dto;
    }

    public ContactDTO toContactDTO(Contact contact) {
        if (contact == null) return null;
        ContactDTO dto = new ContactDTO();
        dto.setId(contact.getId());
        dto.setName(contact.getName());
        dto.setSubject(contact.getSubject());
        dto.setEmail(contact.getEmail());
        dto.setPhone(contact.getPhone());
        dto.setMessage(contact.getMessage());
        dto.setUserSendId(contact.getUserSend() != null ? contact.getUserSend().getId() : null);
        return dto;
    }

    public MessageDTO toMessageDTO(Message message) {
        if (message == null) return null;
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setFullName(message.getFullName());
        dto.setSubject(message.getSubject());
        dto.setMessage(message.getMessage());
        dto.setUserSendId(message.getUserSend() != null ? message.getUserSend().getId() : null);
        dto.setUserReceiveId(message.getUserReceive() != null ? message.getUserReceive().getId() : null);
        return dto;
    }

    public List<AdminDTO> toAdminDTOList(List<Admin> admins) {
        return admins.stream().map(this::toAdminDTO).collect(Collectors.toList());
    }

    public List<CandidateDTO> toCandidateDTOList(List<Candidate> candidates) {
        return candidates.stream().map(this::toCandidateDTO).collect(Collectors.toList());
    }

    public List<RecruiterDTO> toRecruiterDTOList(List<Recruiter> recruiters) {
        return recruiters.stream().map(this::toRecruiterDTO).collect(Collectors.toList());
    }

    public List<UserDTO> toUserDTOList(List<User> users) {
        return users.stream().map(this::toUserDTO).collect(Collectors.toList());
    }

    public List<OfferDTO> toOfferDTOList(List<Offer> offers) {
        return offers.stream().map(this::toOfferDTO).collect(Collectors.toList());
    }

    public List<CandidacyDTO> toCandidacyDTOList(List<Candidacy> candidacies) {
        return candidacies.stream().map(this::toCandidacyDTO).collect(Collectors.toList());
    }

    public List<SkillDTO> toSkillDTOList(List<Skill> skills) {
        return skills.stream().map(this::toSkillDTO).collect(Collectors.toList());
    }

    public List<ExperienceDTO> toExperienceDTOList(List<Experience> experiences) {
        return experiences.stream().map(this::toExperienceDTO).collect(Collectors.toList());
    }

    public List<EducationDTO> toEducationDTOList(List<Education> educations) {
        return educations.stream().map(this::toEducationDTO).collect(Collectors.toList());
    }

    public List<QuestionDTO> toQuestionDTOList(List<Question> questions) {
        return questions.stream().map(this::toQuestionDTO).collect(Collectors.toList());
    }

    public List<FavouriteDTO> toFavouriteDTOList(List<Favourite> favourites) {
        return favourites.stream().map(this::toFavouriteDTO).collect(Collectors.toList());
    }

    public List<ContactDTO> toContactDTOList(List<Contact> contacts) {
        return contacts.stream().map(this::toContactDTO).collect(Collectors.toList());
    }

    public List<MessageDTO> toMessageDTOList(List<Message> messages) {
        return messages.stream().map(this::toMessageDTO).collect(Collectors.toList());
    }

    public User toUserEntity(UserDTO dto) {
        if (dto == null) return null;
        User entity = new User();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setFirstName(dto.getFirstName());
        entity.setEmail(dto.getEmail());
        if (dto.getRole() != null) entity.setRole(Role.valueOf(dto.getRole()));
        return entity;
    }

    public Admin toAdminEntity(AdminDTO dto) {
        if (dto == null) return null;
        Admin entity = new Admin();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setFirstName(dto.getFirstName());
        entity.setEmail(dto.getEmail());
        if (dto.getRole() != null) entity.setRole(Role.valueOf(dto.getRole()));
        entity.setActive(dto.isActive());
        entity.setGender(dto.getGender());
        entity.setBirthdate(dto.getBirthdate());
        entity.setAddress(dto.getAddress());
        entity.setCity(dto.getCity());
        entity.setState(dto.getState());
        entity.setCountry(dto.getCountry());
        entity.setPhone(dto.getPhone());
        entity.setImage(dto.getImage());
        return entity;
    }

    public Candidate toCandidateEntity(CandidateDTO dto) {
        if (dto == null) return null;
        Candidate entity = new Candidate();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setFirstName(dto.getFirstName());
        entity.setEmail(dto.getEmail());
        if (dto.getRole() != null) entity.setRole(Role.valueOf(dto.getRole()));
        entity.setCv(dto.getCv());
        entity.setCvPath(dto.getCvPath());
        entity.setDescription(dto.getDescription());
        entity.setAddress(dto.getAddress());
        entity.setTitle(dto.getTitle());
        entity.setImage(dto.getImage());
        entity.setPhone(dto.getPhone());
        entity.setDateOfBirth(dto.getDateOfBirth());
        entity.setGender(dto.getGender());
        entity.setActive(dto.isActive());
        return entity;
    }

    public Recruiter toRecruiterEntity(RecruiterDTO dto) {
        if (dto == null) return null;
        Recruiter entity = new Recruiter();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setFirstName(dto.getFirstName());
        entity.setEmail(dto.getEmail());
        if (dto.getRole() != null) entity.setRole(Role.valueOf(dto.getRole()));
        entity.setAddress(dto.getAddress());
        entity.setCreationDate(dto.getCreationDate());
        entity.setImage(dto.getImage());
        entity.setActive(dto.isActive());
        entity.setCompanyName(dto.getCompanyName());
        entity.setPhone(dto.getPhone());
        entity.setWebsite(dto.getWebsite());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    public Offer toOfferEntity(OfferDTO dto) {
        if (dto == null) return null;
        Offer entity = new Offer();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setType(dto.getType());
        entity.setAddress(dto.getAddress());
        entity.setSalary(dto.getSalary());
        entity.setExperience(dto.getExperience());
        entity.setPublicationDate(dto.getPublicationDate());
        entity.setExpirationDate(dto.getExpirationDate());
        return entity;
    }

    public Candidacy toCandidacyEntity(CandidacyDTO dto) {
        if (dto == null) return null;
        Candidacy entity = new Candidacy();
        entity.setId(dto.getId());
        entity.setSubmissionDate(dto.getSubmissionDate());
        if (dto.getStatus() != null) entity.setStatus(Status.valueOf(dto.getStatus()));
        entity.setScore(dto.getScore());
        return entity;
    }

    public Skill toSkillEntity(SkillDTO dto) {
        if (dto == null) return null;
        Skill entity = new Skill();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setPercentage(dto.getPercentage());
        return entity;
    }

    public Experience toExperienceEntity(ExperienceDTO dto) {
        if (dto == null) return null;
        Experience entity = new Experience();
        entity.setId(dto.getId());
        entity.setCompanyName(dto.getCompanyName());
        entity.setJobTitle(dto.getJobTitle());
        entity.setStartExpDate(dto.getStartExpDate());
        entity.setEndExpDate(dto.getEndExpDate());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    public Education toEducationEntity(EducationDTO dto) {
        if (dto == null) return null;
        Education entity = new Education();
        entity.setId(dto.getId());
        entity.setDiploma(dto.getDiploma());
        entity.setUniversity(dto.getUniversity());
        entity.setEndDate(dto.getEndDate());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    public Question toQuestionEntity(QuestionDTO dto) {
        if (dto == null) return null;
        Question entity = new Question();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setChoice1(dto.getChoice1());
        entity.setChoice2(dto.getChoice2());
        entity.setChoice3(dto.getChoice3());
        entity.setResponse(dto.getResponse());
        return entity;
    }

    public Favourite toFavouriteEntity(FavouriteDTO dto) {
        if (dto == null) return null;
        Favourite entity = new Favourite();
        entity.setId(dto.getId());
        return entity;
    }

    public Contact toContactEntity(ContactDTO dto) {
        if (dto == null) return null;
        Contact entity = new Contact();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setSubject(dto.getSubject());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setMessage(dto.getMessage());
        return entity;
    }

    public Message toMessageEntity(MessageDTO dto) {
        if (dto == null) return null;
        Message entity = new Message();
        entity.setId(dto.getId());
        entity.setFullName(dto.getFullName());
        entity.setSubject(dto.getSubject());
        entity.setMessage(dto.getMessage());
        return entity;
    }
}
