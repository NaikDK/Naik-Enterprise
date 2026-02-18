package org.naik.trade_journal.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.mongodb.lang.NonNull;

import jakarta.validation.constraints.NotBlank;


public class CloseTradeRequest{

    @NotBlank
    private String id;

    @NonNull
    private BigDecimal exitPrice;

    private BigDecimal exitCommission;

    private LocalDateTime exitDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getExitPrice() {
        return exitPrice;
    }

    public void setExitPrice(BigDecimal exitPrice) {
        this.exitPrice = exitPrice;
    }

    public BigDecimal getExitCommission() {
        return exitCommission;
    }

    public void setExitCommission(BigDecimal exitCommission) {
        this.exitCommission = exitCommission;
    }

    public LocalDateTime getExitDate() {
        return exitDate;
    }

    public void setExitDate(LocalDateTime exitDate) {
        this.exitDate = exitDate;
    }

}