package org.naik.trade_journal.exception;

public class TradeNotFoundException extends RuntimeException{
    public TradeNotFoundException(String tradeId){
        super("Trade not found with trade id: " + tradeId);
    }
}
