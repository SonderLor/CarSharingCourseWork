package ru.sonder.carsharing.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @SequenceGenerator(name = "users_seq", sequenceName = "users_sequence", allocationSize = 1)
    @GeneratedValue(generator = "users_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String username;
    private String password;

    @Column(name = "total_time")
    private Long totalTime;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rent> rents = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn
                    (name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn
                    (name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    public User(String username, String password, Collection<Role> roleAdmin) {
        this.username = username;
        this.password = password;
        this.roles = roleAdmin;
    }

    public Long getTotalTime() {
        if (totalTime == null) {
            return 0L;
        }
        return totalTime;
    }
}
