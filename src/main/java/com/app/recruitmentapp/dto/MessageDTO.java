package com.app.recruitmentapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MessageDTO {
    private Long id;
    @NotBlank(message = "Le nom complet est requis")
    private String fullName;
    @NotBlank(message = "Le sujet est requis")
    private String subject;
    @NotBlank(message = "Le message est requis")
    private String message;
    @NotNull(message = "L'expéditeur est requis")
    private Long userSendId;
    @NotNull(message = "Le destinataire est requis")
    private Long userReceiveId;

    public MessageDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Long getUserSendId() { return userSendId; }
    public void setUserSendId(Long userSendId) { this.userSendId = userSendId; }
    public Long getUserReceiveId() { return userReceiveId; }
    public void setUserReceiveId(Long userReceiveId) { this.userReceiveId = userReceiveId; }
}
