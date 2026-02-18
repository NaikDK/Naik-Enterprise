package org.naik.trade_journal.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.naik.common.model.BaseEntity;
import org.naik.trade_journal.model.enums.TradeStatus;
import org.naik.trade_journal.model.enums.TradeType;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Document(collection="trade")
public class Trade extends BaseEntity{
    
    private String ticker;

    private TradeType type;
    
    private Integer lots;

    @Field(targetType=FieldType.DECIMAL128)
    private BigDecimal strike;

    @Field(targetType=FieldType.DECIMAL128)
    private BigDecimal entryPrice;

    @Field(targetType=FieldType.DECIMAL128)
    private BigDecimal entryCommission;

    private LocalDateTime entryDate;

    @Field(targetType=FieldType.DECIMAL128)
    private BigDecimal exitPrice;

    @Field(targetType=FieldType.DECIMAL128)
    private BigDecimal exitCommission;

    private LocalDateTime exitDate;

    private TradeStatus status;

    private String notes;

    public BigDecimal getPnL(){
        if(exitPrice == null) return BigDecimal.ZERO;

        BigDecimal diff = type == TradeType.BUY 
            ? exitPrice.subtract(entryPrice)
            : entryPrice.subtract(exitPrice);

            return diff.multiply(BigDecimal.valueOf(lots));
    }

    public BigDecimal getPnLPercent(){
        if(exitPrice == null) return BigDecimal.ZERO;

        BigDecimal diff = type == TradeType.BUY
            ? exitPrice.subtract(entryPrice)
            : entryPrice.subtract(exitPrice);

        return diff.divide(entryPrice, 4, java.math.RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
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

    public BigDecimal getStrike() {
        return strike;
    }

    public void setStrike(BigDecimal strike) {
        this.strike = strike;
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

    
    
}
