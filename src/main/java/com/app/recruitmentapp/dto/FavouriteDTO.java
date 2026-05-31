package com.app.recruitmentapp.dto;

import jakarta.validation.constraints.NotNull;

public class FavouriteDTO {
    private Long id;
    @NotNull(message = "L'utilisateur est requis")
    private Long userId;
    @NotNull(message = "L'offre est requise")
    private Long offerId;

    public FavouriteDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getOfferId() { return offerId; }
    public void setOfferId(Long offerId) { this.offerId = offerId; }
}
