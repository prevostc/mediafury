package com.prevostc.mediafury.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.hibernate.validator.constraints.NotBlank;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name="movie")
@EntityListeners(AuditingEntityListener.class)
public class Movie {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @ManyToMany(/* do not cascade anything, manually create each entity */)
    @JoinTable(
        name = "movie_category",
        joinColumns = { @JoinColumn(name = "movie_id") },
        inverseJoinColumns = { @JoinColumn(name = "category_id") }
    )
    private Set<Category> categories = new HashSet<>();

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    public Movie() {
        // for hibernate
    }

    public Movie(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Movie(String title, String description, Category baseCategory) {
        this.title = title;
        this.description = description;
        this.categories.add(baseCategory);
    }

    public String getTitle() {
        return title;
    }

    public Set<Category> getCategories() {
        return categories;
    }
}
