package org.naik.trade_journal.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.naik.trade_journal.dto.CloseTradeRequest;
import org.naik.trade_journal.dto.CreateTradeRequest;
import org.naik.trade_journal.dto.PortfolioSummary;
import org.naik.trade_journal.dto.PortfolioSummary.TickerSummary;
import org.naik.trade_journal.dto.TradeResponse;
import org.naik.trade_journal.exception.TradeAlreadyClosedException;
import org.naik.trade_journal.exception.TradeNotFoundException;
import org.naik.trade_journal.model.Trade;
import org.naik.trade_journal.model.enums.TradeStatus;
import org.naik.trade_journal.repository.TradeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TradeJournalService {
    private final TradeRepository tradeRepository;

    public TradeJournalService(TradeRepository tradeRepository){
        this.tradeRepository = tradeRepository;
    }

    public TradeResponse createTrade(String userId, CreateTradeRequest request){
        Trade trade = new Trade();

        trade.setUserId(userId);
        trade.setTicker(request.getTicker().toUpperCase());
        trade.setType(request.getType());
        trade.setInstrumentType(request.getInstrumentType());
        trade.setLots(request.getLots());
        trade.setStrike(request.getStrike());
        trade.setContractExpiry(request.getContractExpiry());
        trade.setEntryPrice(request.getEntryPrice());
        trade.setEntryCommission(
            request.getEntryCommission() != null ? request.getEntryCommission() : BigDecimal.ZERO
        );
        trade.setEntryDate(
            request.getEntryDate() != null ? request.getEntryDate() : LocalDateTime.now()
        );
        trade.setNotes(request.getNotes());

        if(request.isClosedOnCreate()){
            trade.setExitPrice(request.getExitPrice());
            trade.setExitCommission(
                request.getExitCommission() != null ? request.getExitCommission() : BigDecimal.ZERO
            );
            trade.setExitDate(
                request.getExitDate() != null ? request.getExitDate() : LocalDateTime.now()
            );
            trade.setStatus(TradeStatus.CLOSED);
        } else{
            trade.setStatus(TradeStatus.OPEN);
        }

        Trade saved = tradeRepository.save(trade);
        return toResponse(saved);
    }

    public TradeResponse closeTrade(String userId, String tradeId, CloseTradeRequest request){
        
        Trade trade = tradeRepository.findByIdAndUserId(tradeId, userId)
            .orElseThrow(() -> new TradeNotFoundException(tradeId));

        // Trade trade = tradeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Trade not found"));
        

        if(trade.getStatus() != TradeStatus.OPEN){
            throw new TradeAlreadyClosedException(tradeId);
        }
        trade.setExitPrice(request.getExitPrice());
        trade.setExitCommission(
            request.getExitCommission() != null ? request.getExitCommission() : BigDecimal.ZERO
        );
        trade.setExitDate(
            request.getExitDate() != null ? request.getExitDate() : LocalDateTime.now()
        );

        Trade saved = tradeRepository.save(trade);
        return toResponse(saved);
    }

    // Queries to mongoDB collection

    public List<TradeResponse> getAllTrades(String userId){
        return tradeRepository.findByUserIdOrderByEntryDateDesc(userId)
            .stream().map(this::toResponse)
            .collect(Collectors.toList());
    }

    public List<TradeResponse> getOpenTrades(String userId){
        return tradeRepository.findByUserIdAndStatusOrderByEntryDateDesc(userId, TradeStatus.OPEN)
            .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<TradeResponse> getClosedTrades(String userId){
        return tradeRepository.findByUserIdAndStatusOrderByEntryDateDesc(userId, TradeStatus.CLOSED)
            .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<TradeResponse> getTradesByTicker(String userId, String ticker){
        return tradeRepository.findByUserIdAndTickerOrderByEntryDateDesc(userId, ticker.toUpperCase().trim())
            .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public TradeResponse getTradeById(String userId, String tradeId){
        Trade trade = tradeRepository.findByIdAndUserId(userId, tradeId)
            .orElseThrow(() -> new TradeNotFoundException("Trade not found with trade id: " + tradeId));
            
        return toResponse(trade);
    }

    // Analytics
    public PortfolioSummary getPortfolioSummary(String userId){
        List<Trade> allTrades = tradeRepository.findByUserIdOrderByEntryDateDesc(userId);
        List<Trade> closed = tradeRepository.findClosedTradesByUserId(userId);

        List<BigDecimal> pnls = closed.stream()
            .map(Trade::getPnL)
            .collect(Collectors.toList());

        List<BigDecimal> wins = pnls.stream()
            .filter(p -> p.compareTo(BigDecimal.ZERO) > 0)
            .collect(Collectors.toList());

        List<BigDecimal> losses = pnls.stream()
            .filter(p -> p.compareTo(BigDecimal.ZERO) < 0)
            .collect(Collectors.toList());

        PortfolioSummary summary = new PortfolioSummary();

        summary.setTotalTrades(allTrades.size());
        summary.setClosedTrades(closed.size());
        summary.setOpenTrades(allTrades.size() - closed.size());
        summary.setWinningTrades(wins.size());
        summary.setLoosingTrades(losses.size());
        
        BigDecimal totalPnL = pnls.stream()
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        summary.setTotalPnL(totalPnL);

        BigDecimal totalComm = allTrades.stream()
            .map(Trade::getTotalCommission)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        summary.setTotalCommission(totalComm);

        if(!closed.isEmpty()){
            BigDecimal winRate = BigDecimal.valueOf(wins.size())
                .divide(BigDecimal.valueOf(closed.size()), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
            summary.setWinRate(winRate);
        } else{
            summary.setWinRate(BigDecimal.ZERO);
        }

        BigDecimal avgWin = average(wins);
        BigDecimal avgLoss = average(losses);
        summary.setAvgWin(avgWin);
        summary.setAvgLoss(avgLoss);

        if(!losses.isEmpty() && avgLoss.compareTo(BigDecimal.ZERO) != 0){
            BigDecimal grossWins = wins.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal grossLosses = losses.stream().reduce(BigDecimal.ZERO, BigDecimal::add).abs();
            summary.setProfitFactor(
                grossLosses.compareTo(BigDecimal.ZERO) == 0 
                    ? BigDecimal.ZERO 
                    : grossWins.divide(grossLosses, 2, RoundingMode.HALF_UP)
            );
        } else {
            summary.setProfitFactor(BigDecimal.ZERO);
        }

        if(!closed.isEmpty()){
            summary.setExpectancy(
                totalPnL.divide(BigDecimal.valueOf(closed.size()), 2, RoundingMode.HALF_UP)
            );
        } else{
            summary.setExpectancy(BigDecimal.ZERO);
        }

        wins.stream().max(Comparator.naturalOrder()).ifPresent(summary::setLargestWin);
        losses.stream().min(Comparator.naturalOrder()).ifPresent(summary::setLargestLoss);

        List<TickerSummary> tickerSummaries = buildTickerBreakdown(closed);
        summary.setTickerBreakdown(tickerSummaries);
 
        return summary;


    }


    public TradeResponse toResponse(Trade trade){
        TradeResponse tradeResponse = new TradeResponse();

        tradeResponse.setId(trade.getId());
        tradeResponse.setTicker(trade.getTicker());
        tradeResponse.setType(trade.getType());
        tradeResponse.setLots(trade.getLots());
        tradeResponse.setEntryPrice(trade.getEntryPrice());
        tradeResponse.setEntryCommission(trade.getEntryCommission());
        tradeResponse.setEntryDate(trade.getEntryDate());
        tradeResponse.setExitPrice(trade.getExitPrice());
        tradeResponse.setExitCommission(trade.getExitCommission());
        tradeResponse.setExitDate(trade.getExitDate());
        tradeResponse.setNotes(trade.getNotes());
        tradeResponse.setPnL(trade.getPnL());
        tradeResponse.setPnLPercent(trade.getPnLPercent());

        return tradeResponse;
    }

    private BigDecimal average(List<BigDecimal> values){
        if(values.isEmpty()) return BigDecimal.ZERO;
        BigDecimal sum = values.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(BigDecimal.valueOf(values.size()), 2, RoundingMode.HALF_UP);
    }

    private List<TickerSummary> buildTickerBreakdown(List<Trade> closedTrades){
        Map<String, List<Trade>> byTicker = closedTrades.stream()
            .collect(Collectors.groupingBy(Trade::getTicker));

        return byTicker.entrySet().stream()
            .map(entry -> {
                String ticker = entry.getKey();
                List<Trade> trades = entry.getValue();
                long count = trades.size();

                BigDecimal totalPnL = trades.stream()
                    .map(Trade::getPnL)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                long wins = trades.stream()
                    .filter(t -> t.getPnL().compareTo(BigDecimal.ZERO) > 0)
                    .count();
                
                BigDecimal winRate = count > 0
                    ? BigDecimal.valueOf(wins)
                        .divide(BigDecimal.valueOf(count), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;
                return new TickerSummary(ticker, count, totalPnL, winRate);
            })
            .sorted(Comparator.comparing(TickerSummary::getTotalPnL).reversed())
            .collect(Collectors.toList());
    }
}
