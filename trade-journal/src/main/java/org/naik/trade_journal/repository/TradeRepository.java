package org.naik.trade_journal.repository;

import java.util.List;
import java.util.Optional;

import org.naik.trade_journal.model.Trade;
import org.naik.trade_journal.model.enums.InstrumentType;
import org.naik.trade_journal.model.enums.TradeStatus;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends MongoRepository<Trade, String>{

    Optional<Trade> findByIdAndUserId(String id, String userId);

    List<Trade> findByUserIdOrderByEntryDateDesc(String userId);

    List<Trade> findByUserIdAndStatusOrderByEntryDateDesc(String userId, TradeStatus status);

    List<Trade> findByUserIdAndTickerOrderByEntryDateDesc(String userId, String ticker);

    List<Trade> findByUserIdAndInstrumentTypeOrderByEntryDateDesc(String userId, InstrumentType instrumentType);
    
    @Query("{'userId': ?0, 'status': 'CLOSED'}")
    List<Trade> findClosedTradesByUserId(String userId);

    @Aggregation(pipeline= {
        "{ $match: { 'userId': ?0 } }",
        "{ $group: { _id: '$ticker' } }",
        "{ $sort: { _id: 1 } }",
        "{ $project: { _id: 0, ticker: '$_id' } }"
    })
    List<String> findDistinctTickersByUserId(String userId);

    boolean existsByIdAndUserId(String id, String userIId);

    boolean existsByIdAndStatus(String userId, TradeStatus status);
}
