package com.prevostc.mediafury.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import com.prevostc.mediafury.domain.enumeration.PersonRole;

/**
 * A MoviePerson.
 */
@Entity
@Table(name = "movie_person")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MoviePerson implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_role", nullable = false)
    private PersonRole role;

    @ManyToOne(optional = false)
    @NotNull
    private Movie movie;

    @ManyToOne(optional = false)
    @NotNull
    private Person person;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PersonRole getRole() {
        return role;
    }

    public MoviePerson role(PersonRole role) {
        this.role = role;
        return this;
    }

    public void setRole(PersonRole role) {
        this.role = role;
    }

    public Movie getMovie() {
        return movie;
    }

    public MoviePerson movie(Movie movie) {
        this.movie = movie;
        return this;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Person getPerson() {
        return person;
    }

    public MoviePerson person(Person person) {
        this.person = person;
        return this;
    }

    public void setPerson(Person person) {
        this.person = person;
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
        MoviePerson moviePerson = (MoviePerson) o;
        if (moviePerson.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), moviePerson.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MoviePerson{" +
            "id=" + getId() +
            ", role='" + getRole() + "'" +
            "}";
    }
}
