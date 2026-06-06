package org.naik.trade_journal.exception;

public class TradeAlreadyClosedException extends RuntimeException {
    public TradeAlreadyClosedException(String tradeId){
        super("Trade with trade id " + tradeId + " is already closed.");
    }
}
