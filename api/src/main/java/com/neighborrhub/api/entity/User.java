package com.neighborrhub.api.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "app_user")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String mail;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "neighbourhood_id")
    private Neighbourhood neighbourhood;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name="user_role",
        joinColumns = @JoinColumn(name="user_id"),
        inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Ad> ads = new ArrayList<>();

    @OneToMany(mappedBy = "sender")
    private List<Message> sentMessages = new ArrayList<>();

    @OneToMany(mappedBy = "receiver")
    private List<Message> receiverMessages = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Event> events = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Rsvp> rsvps = new ArrayList<>();
}
