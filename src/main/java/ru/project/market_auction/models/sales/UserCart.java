package ru.project.market_auction.models.sales;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import ru.project.market_auction.models.users.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_cart")
@AllArgsConstructor
public class UserCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "userCart")
    private List<UserCartDetail> details;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<UserCartDetail> getDetails() {
        return details;
    }

    public void setDetails(List<UserCartDetail> details) {
        this.details = details;
    }

    public UserCart(){
        this.details = new ArrayList<>();
    }
}
