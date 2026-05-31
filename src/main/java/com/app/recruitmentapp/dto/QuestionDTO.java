package com.app.recruitmentapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class QuestionDTO {
    private Long id;
    @NotBlank(message = "Le titre est requis")
    private String title;
    @NotBlank(message = "Le choix 1 est requis")
    private String choice1;
    @NotBlank(message = "Le choix 2 est requis")
    private String choice2;
    private String choice3;
    @NotBlank(message = "La réponse est requise")
    private String response;
    @NotNull(message = "L'offre est requise")
    private Long offerId;

    public QuestionDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getChoice1() { return choice1; }
    public void setChoice1(String choice1) { this.choice1 = choice1; }
    public String getChoice2() { return choice2; }
    public void setChoice2(String choice2) { this.choice2 = choice2; }
    public String getChoice3() { return choice3; }
    public void setChoice3(String choice3) { this.choice3 = choice3; }
    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }
    public Long getOfferId() { return offerId; }
    public void setOfferId(Long offerId) { this.offerId = offerId; }
}
