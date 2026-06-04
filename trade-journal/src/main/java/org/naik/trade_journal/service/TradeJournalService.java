package org.naik.trade_journal.service;

import java.util.List;
import java.util.stream.Collectors;

import org.naik.trade_journal.dto.CloseTradeRequest;
import org.naik.trade_journal.dto.CreateTradeRequest;
import org.naik.trade_journal.dto.TradeResponse;
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

    public TradeResponse createTrade(CreateTradeRequest request){
        Trade trade = new Trade();

        trade.setUserId(userId);
        trade.setTicker(request.getTicker().toUpperCase());
        trade.setType(request.getType());
        trade.setLots(request.getLots());
        trade.setEntryPrice(request.getEntryPrice());
        trade.setEntryCommission(request.getEntryCommission());
        trade.setEntryDate(request.getEntryDate());
        trade.setNotes(request.getNotes());
        trade.setStatus(request.getStatus());

        Trade saved = tradeRepository.save(trade);
        return toResponse(saved);
    }

    public TradeResponse closeTrade(String id, CloseTradeRequest request){
        Trade trade = tradeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Trade not found"));

        if(trade.getStatus() != TradeStatus.OPEN){
            throw new IllegalArgumentException("Trade is already closed!");
        }
        trade.setExitPrice(request.getExitPrice());
        trade.setExitCommission(request.getExitCommission());
        trade.setExitDate(request.getExitDate());

        Trade saved = tradeRepository.save(trade);
        return toResponse(saved);
    }

    public List<TradeResponse> getAllTrades(){
        return tradeRepository.findAllOrderByEntryDateDesc()
            .stream().map(this::toResponse)
            .collect(Collectors.toList());
    }

    public List<TradeResponse> getOpenTrades(){
        return tradeRepository.findByStatusOrderByEntryDateDesc(TradeStatus.OPEN)
            .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<TradeResponse> getTradesByTicker(String ticker){
        return tradeRepository.findByTickerOrderByEntryDateDesc(ticker.toUpperCase())
            .stream().map(this::toResponse).collect(Collectors.toList());
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
}
