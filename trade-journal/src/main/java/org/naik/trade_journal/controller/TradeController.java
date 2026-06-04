package org.naik.trade_journal.controller;

import java.util.List;

import org.naik.common.dto.ApiResponse;
import org.naik.trade_journal.dto.CloseTradeRequest;
import org.naik.trade_journal.dto.CreateTradeRequest;
import org.naik.trade_journal.dto.TradeResponse;
import org.naik.trade_journal.service.TradeJournalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping("/api/trades")
@CrossOrigin(origins="*")
public class TradeController {
    private final TradeJournalService tradeJournalService;

    public TradeController(TradeJournalService tradeJournalService){
        this.tradeJournalService = tradeJournalService;
    }

    /**
     * Returns the health of the trade journal service
     * 
     * @return
     * String indicating the endpoint is healthy and ready to serve requests.
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("Trade Journal endpoint is healthy!"));
    }

    /**
     * 
     * @param 
     *  reuqest - request includes trade details for creating new trade
     * @return
     *  TradeResponse with trade ID and other supplied details.
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<TradeResponse>> createTrade(@RequestBody CreateTradeRequest reuqest) {
        return ResponseEntity.ok(
            ApiResponse.success(
                "Trade created successfully!", tradeJournalService.createTrade(reuqest)
            )
        );
    }

    /**
     * 
     * @param 
     *  id - the id of the trade to be closed
     *  request - Exit trade details such as price, commission and date.
     * @return
     *  TradeResponse - includes all details of the trade 
     */
    @PutMapping("close/{id}")
    public ResponseEntity<ApiResponse<TradeResponse>> closeTrade(
        @PathVariable String id, 
        @RequestBody CloseTradeRequest request) {
        
        return ResponseEntity.ok(
            ApiResponse.success(
                "Trade closed successfully!", tradeJournalService.closeTrade(
                    id, request
                )
            )
        );
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<TradeResponse>>> getAllTrades() {
        return ResponseEntity.ok(
            ApiResponse.success(
                tradeJournalService.getAllTrades()
            )
        );
    }

    @GetMapping("/open")
    public ResponseEntity<ApiResponse<List<TradeResponse>>> getOpenTrades() {
        return ResponseEntity.ok(
            ApiResponse.success(
                tradeJournalService.getOpenTrades()
            )
        );
    }
    
    @GetMapping("/ticker/{ticker}")
    public ResponseEntity<ApiResponse<List<TradeResponse>>> getTradesByTicker(
        @PathVariable String ticker) {
        
            return ResponseEntity.ok(
                ApiResponse.success(
                    tradeJournalService.getTradesByTicker(ticker)
                )
            );
    }
    
    
}
