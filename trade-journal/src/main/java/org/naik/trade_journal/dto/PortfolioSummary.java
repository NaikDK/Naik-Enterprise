package org.naik.trade_journal.dto;

import java.math.BigDecimal;
import java.util.List;

public class PortfolioSummary {
    private long totalTrades;
    private long openTrades;
    private long closedTrades;
    private long winningTrades;
    private long loosingTrades;

    private BigDecimal totalPnL;
    private BigDecimal totalCommission;
    private BigDecimal winRate;
    private BigDecimal avgWin;
    private BigDecimal avgLoss;
    private BigDecimal profitFactor;
    private BigDecimal expectancy;
    private BigDecimal largestWin;
    private BigDecimal largestLoss;

    private List<TickerSummary> tickerBreakdown;


    //  supporting classe for listing trades by ticker
    public static class TickerSummary{
        private String ticker;
        private long tradeCount;
        private BigDecimal totalPnL;
        private BigDecimal winRate;

        public TickerSummary(String ticker, long tradeCount, BigDecimal totalPnL, BigDecimal winRate){
            this.ticker = ticker;
            this.tradeCount = tradeCount;
            this.totalPnL = totalPnL;
            this.winRate = winRate;
        }

        public String getTicker() { return ticker; }
        public long getTradeCount() { return tradeCount; }
        public BigDecimal getTotalPnL() { return totalPnL; }
        public BigDecimal getWinRate() { return winRate; }

    }

    public long getTotalTrades() {
        return totalTrades;
    }

    public void setTotalTrades(long totalTrades) {
        this.totalTrades = totalTrades;
    }

    public long getOpenTrades() {
        return openTrades;
    }

    public void setOpenTrades(long openTrades) {
        this.openTrades = openTrades;
    }

    public long getClosedTrades() {
        return closedTrades;
    }

    public void setClosedTrades(long closedTrades) {
        this.closedTrades = closedTrades;
    }

    public long getWinningTrades() {
        return winningTrades;
    }

    public void setWinningTrades(long winningTrades) {
        this.winningTrades = winningTrades;
    }

    public long getLoosingTrades() {
        return loosingTrades;
    }

    public void setLoosingTrades(long loosingTrades) {
        this.loosingTrades = loosingTrades;
    }

    public BigDecimal getTotalPnL() {
        return totalPnL;
    }

    public void setTotalPnL(BigDecimal totalPnL) {
        this.totalPnL = totalPnL;
    }

    public BigDecimal getTotalCommission() {
        return totalCommission;
    }

    public void setTotalCommission(BigDecimal totalCommission) {
        this.totalCommission = totalCommission;
    }

    public BigDecimal getWinRate() {
        return winRate;
    }

    public void setWinRate(BigDecimal winRate) {
        this.winRate = winRate;
    }

    public BigDecimal getAvgWin() {
        return avgWin;
    }

    public void setAvgWin(BigDecimal avgWin) {
        this.avgWin = avgWin;
    }

    public BigDecimal getAvgLoss() {
        return avgLoss;
    }

    public void setAvgLoss(BigDecimal avgLoss) {
        this.avgLoss = avgLoss;
    }

    public BigDecimal getProfitFactor() {
        return profitFactor;
    }

    public void setProfitFactor(BigDecimal profitFactor) {
        this.profitFactor = profitFactor;
    }

    public BigDecimal getExpectancy() {
        return expectancy;
    }

    public void setExpectancy(BigDecimal expectancy) {
        this.expectancy = expectancy;
    }

    public BigDecimal getLargestWin() {
        return largestWin;
    }

    public void setLargestWin(BigDecimal largestWin) {
        this.largestWin = largestWin;
    }

    public BigDecimal getLargestLoss() {
        return largestLoss;
    }

    public void setLargestLoss(BigDecimal largestLoss) {
        this.largestLoss = largestLoss;
    }

    public List<TickerSummary> getTickerBreakdown() {
        return tickerBreakdown;
    }

    public void setTickerBreakdown(List<TickerSummary> tickerBreakdown) {
        this.tickerBreakdown = tickerBreakdown;
    }
}
