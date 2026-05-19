package com.neighborrhub.api.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "app_neighbourhood", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "zipcode"}))
@Data
@NoArgsConstructor
public class Neighbourhood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String zipcode;

    @OneToMany(mappedBy = "neighbourhood")
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "neighbourhood", cascade = CascadeType.ALL)
    private List<Event> events = new ArrayList<>();
}