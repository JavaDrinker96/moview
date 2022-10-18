package com.example.moview.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
public class Movie extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_movie")
    @SequenceGenerator(name = "seq_movie", sequenceName = "SEQ_MOVIE", allocationSize = 10)
    private Long id;

    @Column(columnDefinition = "varchar(352) not null")
    private String title;

    @Setter
    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private LocalDate releaseDate;

    @Column(nullable = false)
    private Duration duration;

    @Setter
    private Integer rating;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Review> reviews;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    @ToString.Exclude
    private Set<Genre> genres;
}