package org.naik.trade_journal.repository;

import java.util.List;

import org.naik.trade_journal.model.Trade;
import org.naik.trade_journal.model.enums.TradeStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends MongoRepository<Trade, String>{
    List<Trade> findByTickerOrderByEntryDateDesc(String ticker);
    List<Trade> findByStatusOrderByEntryDateDesc(TradeStatus status);

    @Query(value = "{}", sort = "{'entryDate': -1}")
    List<Trade> findAllOrderByEntryDateDesc();
}
