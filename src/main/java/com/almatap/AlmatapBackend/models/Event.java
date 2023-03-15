package com.almatap.AlmatapBackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
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

    @NotEmpty(message = "Name should be not empty")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "description should be not empty")
    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "category")
    private String category;

    @Column(name = "price")
    private int price;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "expire_at")
    @Temporal(TemporalType.DATE)
    private Date expireAt;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Rating> rating;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    @Transient
    private double averageRating;

    @PrePersist
    private void createdAt(){
        createdAt = new Date();
    }

    @PreUpdate
    private void updatedAt(){
        updatedAt = new Date();
    }

    public void addImage(Image image){
        image.setEvent(this);
        images.add(image);
    }
}
