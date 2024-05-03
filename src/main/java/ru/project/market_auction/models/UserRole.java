package ru.project.market_auction.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_role")
@Data
@NoArgsConstructor
@IdClass(UserRoleId.class)
public class UserRole {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public UserRole(User user, Role role){
        this.user = user;
        this.role = role;
    }
}
