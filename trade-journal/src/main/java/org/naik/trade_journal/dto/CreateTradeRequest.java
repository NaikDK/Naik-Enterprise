package org.naik.trade_journal.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.naik.trade_journal.model.enums.InstrumentType;
import org.naik.trade_journal.model.enums.TradeStatus;
import org.naik.trade_journal.model.enums.TradeType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateTradeRequest {
    
    @NotBlank(message="Stock Symbol is mandatory.")
    private String ticker;
    
    @NotNull(message="Please select Trade Type (Buy/Sell)")
    private TradeType type;

    @NotNull(message="Please select Instrument Type (OPTION/STOCK/FUTURES)")
    private InstrumentType instrumentType;

    @Positive(message="Lots must be greater than 0.")
    private Integer lots = 1;

    private BigDecimal strike;

    private LocalDateTime contractExpiry;

    @NotNull(message="Entry price is mandatory.")
    private BigDecimal entryPrice;

    private BigDecimal entryCommission;

    private LocalDateTime entryDate;

    @NotNull(message="Exit price is mandatory.")
    private BigDecimal exitPrice;

    private BigDecimal exitCommission;

    private LocalDateTime exitDate;

    private TradeStatus status;

    private String notes;

    public boolean isClosedOnCreate() {
        return exitPrice != null;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public TradeType getType() {
        return type;
    }

    public void setType(TradeType type) {
        this.type = type;
    }

    public Integer getLots() {
        return lots;
    }

    public void setLots(Integer lots) {
        this.lots = lots;
    }

    public BigDecimal getEntryPrice() {
        return entryPrice;
    }

    public void setEntryPrice(BigDecimal entryPrice) {
        this.entryPrice = entryPrice;
    }

    public BigDecimal getEntryCommission() {
        return entryCommission;
    }

    public void setEntryCommission(BigDecimal entryCommission) {
        this.entryCommission = entryCommission;
    }

    public LocalDateTime getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDateTime entryDate) {
        this.entryDate = entryDate;
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
    
    public TradeStatus getStatus() {
        return status;
    }

    public void setStatus(TradeStatus status) {
        this.status = status;
    }
    
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public InstrumentType getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(InstrumentType instrumentType) {
        this.instrumentType = instrumentType;
    }

    public BigDecimal getStrike() {
        return strike;
    }

    public void setStrike(BigDecimal strike) {
        this.strike = strike;
    }

    public LocalDateTime getContractExpiry() {
        return contractExpiry;
    }

    public void setContractExpiry(LocalDateTime contractExpiry) {
        this.contractExpiry = contractExpiry;
    }
    
}
