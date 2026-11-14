package com.cinestream.dto;

import com.cinestream.model.RentalStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RentalDto {
    private Long id;
    private Long userId;
    private Long mediaId;
    private String mediaTitle;
    private String mediaPosterUrl;
    private String mediaType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer days;
    private BigDecimal totalPrice;
    private RentalStatus status;
    private Long daysLeft;
    private Long hoursLeft;
    private boolean active;

    public RentalDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaTitle() {
        return mediaTitle;
    }

    public void setMediaTitle(String mediaTitle) {
        this.mediaTitle = mediaTitle;
    }

    public String getMediaPosterUrl() {
        return mediaPosterUrl;
    }

    public void setMediaPosterUrl(String mediaPosterUrl) {
        this.mediaPosterUrl = mediaPosterUrl;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public RentalStatus getStatus() {
        return status;
    }

    public void setStatus(RentalStatus status) {
        this.status = status;
    }

    public Long getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(Long daysLeft) {
        this.daysLeft = daysLeft;
    }

    public Long getHoursLeft() {
        return hoursLeft;
    }

    public void setHoursLeft(Long hoursLeft) {
        this.hoursLeft = hoursLeft;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
