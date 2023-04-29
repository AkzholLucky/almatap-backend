package com.almatap.AlmatapBackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "app_rating")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppRating {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date_post")
    @Temporal(TemporalType.DATE)
    private Date datePost;

    @Column(name = "rating")
    private int rating;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
