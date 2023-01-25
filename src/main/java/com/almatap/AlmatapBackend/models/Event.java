package com.almatap.AlmatapBackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "events")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Event {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "category")
    private String category;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Lob
    @Column(name = "image",columnDefinition = "MEDIUMBLOB")
    private String image;

    @OneToMany(mappedBy = "event")
    private List<Rating> rating;

    @PrePersist
    private void createdAt(){
        createdAt = new Date();
    }

    @PreUpdate
    private void updatedAt(){
        updatedAt = new Date();
    }
}
