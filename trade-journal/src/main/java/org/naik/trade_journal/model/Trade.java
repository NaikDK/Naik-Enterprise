package org.naik.trade_journal.model;

import java.math.BigDecimal;
import static java.math.BigDecimal.ZERO;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import org.naik.common.model.BaseEntity;
import org.naik.trade_journal.model.enums.InstrumentType;
import org.naik.trade_journal.model.enums.TradeStatus;
import org.naik.trade_journal.model.enums.TradeType;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Document(collection="trade")
@CompoundIndexes({
    @CompoundIndex(name = "idx_userId_entryDate", def = "{'userId': 1, 'entryDate': -1}"),
    @CompoundIndex(name = "idx_userId_status", def = "{'userId': 1, 'status': 1}"),
    @CompoundIndex(name = "idx_userId_ticker", def = "{'userId': 1, 'ticker': 1}"),
    @CompoundIndex(name = "idx_userId_instrumentType", def = "{'userId': 1, 'instrumentType': 1}")
})
public class Trade extends BaseEntity{
    
    private String ticker;

    private TradeType type;
    
    private Integer lots;

    private InstrumentType instrumentType;

    @Field(targetType=FieldType.DECIMAL128)
    private BigDecimal strike;

    private LocalDateTime contractExpiry;

    // Entry leg

    @Field(targetType=FieldType.DECIMAL128)
    private BigDecimal entryPrice;

    @Field(targetType=FieldType.DECIMAL128)
    private BigDecimal entryCommission;

    private LocalDateTime entryDate;

    // Exit Leg

    @Field(targetType=FieldType.DECIMAL128)
    private BigDecimal exitPrice;

    @Field(targetType=FieldType.DECIMAL128)
    private BigDecimal exitCommission;

    private LocalDateTime exitDate;

    // Trade details

    private TradeStatus status;

    private String notes;

    //
    public BigDecimal getPnL(){
        if(exitPrice == null) return BigDecimal.ZERO;

        int multiplier = (instrumentType == InstrumentType.OPTION)? 100 : 1;

        BigDecimal priceDiff = type == TradeType.BUY 
            ? exitPrice.subtract(entryPrice)
            : entryPrice.subtract(exitPrice);

        BigDecimal grossPnL = priceDiff
            .multiply(BigDecimal.valueOf(lots))
            .multiply(BigDecimal.valueOf(multiplier));

        BigDecimal totalComm = (entryCommission != null ? entryCommission : ZERO)
            .add(exitCommission != null ? exitCommission : ZERO);

        return grossPnL.subtract(totalComm).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getPnLPercent(){
        if(exitPrice == null || entryPrice == null
                || entryPrice.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        int multiplier = (instrumentType == InstrumentType.OPTION)? 100 : 1;

        BigDecimal entryCost = entryPrice
            .multiply(BigDecimal.valueOf(lots))
            .multiply(BigDecimal.valueOf(multiplier));

        return getPnL()
            .divide(entryCost, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100))
            .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalCommission(){
        return (entryCommission != null ? entryCommission : ZERO)
            .add(exitCommission != null ? exitCommission : ZERO);
    }
    
    // Getters and Setters

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
