package com.cinestream.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class TopupRequest {

    @NotNull(message = "Le montant de recharge est requis")
    @DecimalMin(value = "1.00", message = "Le montant minimum de recharge est de 1.00 €")
    @DecimalMax(value = "500.00", message = "Le montant maximum de recharge est de 500.00 €")
    private BigDecimal amount;

    public TopupRequest() {
    }

    public TopupRequest(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
