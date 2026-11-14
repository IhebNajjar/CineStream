package com.cinestream.dto;

import com.cinestream.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDto {
    private Long id;
    private BigDecimal amount;
    private TransactionType type;
    private String description;
    private LocalDateTime createdAt;

    public TransactionDto() {
    }

    public TransactionDto(Long id, BigDecimal amount, TransactionType type, String description, LocalDateTime createdAt) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
