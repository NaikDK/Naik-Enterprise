package org.naik.trade_journal.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class CloseTradeRequest{

    @NotBlank
    private String id;

    @NotNull
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