package com.app.recruitmentapp.mapper;

import com.app.recruitmentapp.dto.*;
import com.app.recruitmentapp.entities.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @org.mapstruct.Builder(disableBuilder = true))
public interface EntityMapper {

    // ========== User ==========
    @Mapping(target = "role", expression = "java(user.getRole() != null ? user.getRole().name() : null)")
    UserDTO toUserDTO(User user);

    @Mapping(target = "role", expression = "java(dto.getRole() != null ? com.app.recruitmentapp.entities.Role.valueOf(dto.getRole()) : null)")
    User toUserEntity(UserDTO dto);

    List<UserDTO> toUserDTOList(List<User> users);

    // ========== Admin ==========
    @Mapping(target = "role", expression = "java(admin.getRole() != null ? admin.getRole().name() : null)")
    AdminDTO toAdminDTO(Admin admin);

    @Mapping(target = "role", expression = "java(dto.getRole() != null ? com.app.recruitmentapp.entities.Role.valueOf(dto.getRole()) : null)")
    Admin toAdminEntity(AdminDTO dto);

    List<AdminDTO> toAdminDTOList(List<Admin> admins);

    // ========== Candidate ==========
    @Mapping(target = "role", expression = "java(candidate.getRole() != null ? candidate.getRole().name() : null)")
    CandidateDTO toCandidateDTO(Candidate candidate);

    @Mapping(target = "role", expression = "java(dto.getRole() != null ? com.app.recruitmentapp.entities.Role.valueOf(dto.getRole()) : null)")
    Candidate toCandidateEntity(CandidateDTO dto);

    List<CandidateDTO> toCandidateDTOList(List<Candidate> candidates);

    // ========== Recruiter ==========
    @Mapping(target = "role", expression = "java(recruiter.getRole() != null ? recruiter.getRole().name() : null)")
    RecruiterDTO toRecruiterDTO(Recruiter recruiter);

    @Mapping(target = "role", expression = "java(dto.getRole() != null ? com.app.recruitmentapp.entities.Role.valueOf(dto.getRole()) : null)")
    Recruiter toRecruiterEntity(RecruiterDTO dto);

    List<RecruiterDTO> toRecruiterDTOList(List<Recruiter> recruiters);

    // ========== Offer ==========
    @Mapping(target = "recruiterId", expression = "java(offer.getRecruiter() != null ? offer.getRecruiter().getId() : null)")
    OfferDTO toOfferDTO(Offer offer);

    Offer toOfferEntity(OfferDTO dto);

    List<OfferDTO> toOfferDTOList(List<Offer> offers);

    // ========== Candidacy ==========
    @Mapping(target = "status", expression = "java(candidacy.getStatus() != null ? candidacy.getStatus().name() : null)")
    @Mapping(target = "candidateId", expression = "java(candidacy.getCandidate() != null ? candidacy.getCandidate().getId() : null)")
    @Mapping(target = "offerId", expression = "java(candidacy.getOffer() != null ? candidacy.getOffer().getId() : null)")
    CandidacyDTO toCandidacyDTO(Candidacy candidacy);

    @Mapping(target = "status", expression = "java(dto.getStatus() != null ? com.app.recruitmentapp.entities.Status.valueOf(dto.getStatus()) : null)")
    Candidacy toCandidacyEntity(CandidacyDTO dto);

    List<CandidacyDTO> toCandidacyDTOList(List<Candidacy> candidacies);

    // ========== Skill ==========
    @Mapping(target = "candidateId", expression = "java(skill.getCandidate() != null ? skill.getCandidate().getId() : null)")
    SkillDTO toSkillDTO(Skill skill);

    Skill toSkillEntity(SkillDTO dto);

    List<SkillDTO> toSkillDTOList(List<Skill> skills);

    // ========== Experience ==========
    @Mapping(target = "candidateId", expression = "java(experience.getCandidate() != null ? experience.getCandidate().getId() : null)")
    ExperienceDTO toExperienceDTO(Experience experience);

    Experience toExperienceEntity(ExperienceDTO dto);

    List<ExperienceDTO> toExperienceDTOList(List<Experience> experiences);

    // ========== Education ==========
    @Mapping(target = "candidateId", expression = "java(education.getCandidate() != null ? education.getCandidate().getId() : null)")
    EducationDTO toEducationDTO(Education education);

    Education toEducationEntity(EducationDTO dto);

    List<EducationDTO> toEducationDTOList(List<Education> educations);

    // ========== Question ==========
    @Mapping(target = "offerId", expression = "java(question.getOffer() != null ? question.getOffer().getId() : null)")
    QuestionDTO toQuestionDTO(Question question);

    Question toQuestionEntity(QuestionDTO dto);

    List<QuestionDTO> toQuestionDTOList(List<Question> questions);

    // ========== Favourite ==========
    @Mapping(target = "userId", expression = "java(favourite.getUser() != null ? favourite.getUser().getId() : null)")
    @Mapping(target = "offerId", expression = "java(favourite.getOffer() != null ? favourite.getOffer().getId() : null)")
    FavouriteDTO toFavouriteDTO(Favourite favourite);

    Favourite toFavouriteEntity(FavouriteDTO dto);

    List<FavouriteDTO> toFavouriteDTOList(List<Favourite> favourites);

    // ========== Contact ==========
    @Mapping(target = "userSendId", expression = "java(contact.getUserSend() != null ? contact.getUserSend().getId() : null)")
    ContactDTO toContactDTO(Contact contact);

    Contact toContactEntity(ContactDTO dto);

    List<ContactDTO> toContactDTOList(List<Contact> contacts);

    // ========== Message ==========
    @Mapping(target = "userSendId", expression = "java(message.getUserSend() != null ? message.getUserSend().getId() : null)")
    @Mapping(target = "userReceiveId", expression = "java(message.getUserReceive() != null ? message.getUserReceive().getId() : null)")
    MessageDTO toMessageDTO(Message message);

    Message toMessageEntity(MessageDTO dto);

    List<MessageDTO> toMessageDTOList(List<Message> messages);
}
