package org.naik.trade_journal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"org.naik.common", "org.naik.trade_journal"})
public class TradeJournalApplication {
    public static void main(String[] args) {
        SpringApplication.run(TradeJournalApplication.class, args);
    }
}
