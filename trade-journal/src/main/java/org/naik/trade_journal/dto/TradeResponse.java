package org.naik.trade_journal.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.naik.trade_journal.model.enums.InstrumentType;
import org.naik.trade_journal.model.enums.TradeStatus;
import org.naik.trade_journal.model.enums.TradeType;

public class TradeResponse {
    private String id;
    private String ticker;
    private TradeType type;
    private InstrumentType instrumentType;
    private Integer lots;
    private BigDecimal strike;
    private LocalDateTime contractExpiry;
    // Entry leg
    private BigDecimal entryPrice;
    private BigDecimal entryCommission;
    private LocalDateTime entryDate;

    // Exit Leg
    private BigDecimal exitPrice;
    private BigDecimal exitCommission;
    private LocalDateTime exitDate;

    // Trade detaila
    private TradeStatus status;
    private String notes;

    // Computations
    private BigDecimal PnL;
    private BigDecimal PnLPercent;
    private BigDecimal totalCommission;

    //Getters and Setters
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public BigDecimal getPnL() {
        return PnL;
    }
    public void setPnL(BigDecimal pnL) {
        PnL = pnL;
    }
    public BigDecimal getPnLPercent() {
        return PnLPercent;
    }
    public void setPnLPercent(BigDecimal pnLPercent) {
        PnLPercent = pnLPercent;
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
    public BigDecimal getTotalCommission() {
        return totalCommission;
    }
    public void setTotalCommission(BigDecimal totalCommission) {
        this.totalCommission = totalCommission;
    }
    
}
