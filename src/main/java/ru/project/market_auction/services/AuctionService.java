package ru.project.market_auction.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.project.market_auction.models.auctions.Auction;
import ru.project.market_auction.models.auctions.AuctionBid;
import ru.project.market_auction.repositories.AuctionBidRepository;
import ru.project.market_auction.repositories.AuctionRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.SimpleFormatter;

@Service
public class AuctionService {
    @Autowired private AuctionRepository auctionRepository;
    @Autowired private AuctionBidRepository auctionBidRepository;

    @Transactional
    @Scheduled(fixedDelay = 60000) // Запускать каждую минуту
    public void checkLBAndEndAuctions() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        List<Auction> auctions = auctionRepository.findByAuctionTypeAndStatus("lb", false);
        for (Auction auction : auctions) {
            AuctionBid lastBid = auctionBidRepository.findTopByAuctionOrderByBidTimeDesc(auction);
            if (lastBid != null) {
                LocalDateTime lastBidTime = lastBid.getBidTime();
                System.out.println("1: " + lastBidTime);
                LocalDateTime currentTime = LocalDateTime.now();
                System.out.println("2: " + currentTime);
                Duration duration = Duration.between(lastBidTime, currentTime);
                if (duration.toMinutes() >= 5) {
                    auction.setStatus(true); // Завершить аукцион
                    String time = currentTime.format(formatter);
                    auction.setEndTime(time);
                    auctionRepository.save(auction);
                }
            }
        }
    }

    @Transactional
    @Scheduled(fixedDelay = 1000) // Запускать каждую секунду
    public void checkLTAndEndAuctions() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        List<Auction> auctions = auctionRepository.findByAuctionTypeAndStatus("lt", false);
        auctions.parallelStream().forEach(auction -> {
            if(!auction.getAuctionBids().isEmpty()){
                LocalTime currentDuration = auction.getAuctionCDuration();
                if (currentDuration != null && currentDuration != LocalTime.MIDNIGHT) {
                    LocalTime newDuration = currentDuration.minusSeconds(1L);

                    if (newDuration.equals(LocalTime.MIDNIGHT)) {
                        auction.setAuctionCDuration(LocalTime.MIDNIGHT);
                        auction.setStatus(true); // Завершить аукцион
                        String time = LocalDateTime.now().format(formatter);
                        auction.setEndTime(time);
                    } else {
                        auction.setAuctionCDuration(newDuration);
                    }

                    auctionRepository.save(auction);
                }
            }
        });
    }
}
