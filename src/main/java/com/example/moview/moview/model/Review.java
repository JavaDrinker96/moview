package com.example.moview.moview.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import java.time.LocalDate;


@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_seq")
    @SequenceGenerator(name = "review_seq", sequenceName = "SEQ_REVIEW", allocationSize = 10)
    private Long id;

    @Range(min = 1, max = 100)
    private Integer score;

    @Column(columnDefinition = "varchar(352) not null")
    private String title;

    @Column(columnDefinition = "text not null")
    private String content;

    @Setter
    @Column(name = "publication_date", updatable = false, nullable = false)
    private LocalDate publicationDate;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Setter
    @ManyToOne
    @JoinColumn(name = "app_user_id", nullable = false)
    private User author;
}