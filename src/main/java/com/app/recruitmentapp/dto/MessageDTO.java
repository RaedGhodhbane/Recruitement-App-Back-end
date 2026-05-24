package com.app.recruitmentapp.dto;

public class MessageDTO {
    private Long id;
    private String fullName;
    private String subject;
    private String message;
    private Long userSendId;
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
