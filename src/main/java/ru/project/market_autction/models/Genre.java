package ru.projects.market_auction.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "genres")
@Data
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
}