package com.prevostc.mediafury.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Movie.
 */
@Entity
@Table(name = "movie")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Movie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Min(value = 1888)
    @Max(value = 3000)
    @Column(name = "jhi_year")
    private Integer year;

    @Size(max = 10000)
    @Lob
    @Column(name = "plot", length = 10000)
    private String plot;

    @Size(max = 2000)
    @Column(name = "image_url", length = 2000)
    private String imageUrl;

    @Column(name = "elo")
    private Integer elo;

    @OneToMany(mappedBy = "movie")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MoviePerson> moviePeople = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "movie_category",
               joinColumns = @JoinColumn(name="movies_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="categories_id", referencedColumnName="id"))
    private Set<Category> categories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Movie title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public Movie year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getPlot() {
        return plot;
    }

    public Movie plot(String plot) {
        this.plot = plot;
        return this;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Movie imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getElo() {
        return elo;
    }

    public Movie elo(Integer elo) {
        this.elo = elo;
        return this;
    }

    public void setElo(Integer elo) {
        this.elo = elo;
    }

    public Set<MoviePerson> getMoviePeople() {
        return moviePeople;
    }

    public Movie moviePeople(Set<MoviePerson> moviePeople) {
        this.moviePeople = moviePeople;
        return this;
    }

    public Movie addMoviePerson(MoviePerson moviePerson) {
        this.moviePeople.add(moviePerson);
        moviePerson.setMovie(this);
        return this;
    }

    public Movie removeMoviePerson(MoviePerson moviePerson) {
        this.moviePeople.remove(moviePerson);
        moviePerson.setMovie(null);
        return this;
    }

    public void setMoviePeople(Set<MoviePerson> moviePeople) {
        this.moviePeople = moviePeople;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public Movie categories(Set<Category> categories) {
        this.categories = categories;
        return this;
    }

    public Movie addCategory(Category category) {
        this.categories.add(category);
        category.getMovies().add(this);
        return this;
    }

    public Movie removeCategory(Category category) {
        this.categories.remove(category);
        category.getMovies().remove(this);
        return this;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Movie movie = (Movie) o;
        if (movie.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), movie.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Movie{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", year=" + getYear() +
            ", plot='" + getPlot() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", elo=" + getElo() +
            "}";
    }
}
