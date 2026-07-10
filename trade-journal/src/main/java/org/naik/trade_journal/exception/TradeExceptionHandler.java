package org.naik.trade_journal.exception;

import org.naik.common.dto.ApiResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(1)
public class TradeExceptionHandler {
    @ExceptionHandler(TradeAlreadyClosedException.class)
    public ResponseEntity<ApiResponse<Void>> handleTradeAlreadyClosedException(TradeAlreadyClosedException e){
        return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(ApiResponse.error(e.getMessage()));
    }

    @ExceptionHandler(TradeNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleTradeNotFoundException(TradeNotFoundException e){
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(e.getMessage()));
    }

}
