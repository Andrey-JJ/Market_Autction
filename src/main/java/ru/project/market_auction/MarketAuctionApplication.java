package ru.project.market_auction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MarketAuctionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketAuctionApplication.class, args);
	}

}
