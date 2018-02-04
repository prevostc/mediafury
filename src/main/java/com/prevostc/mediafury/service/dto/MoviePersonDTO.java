package com.prevostc.mediafury.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.prevostc.mediafury.domain.enumeration.PersonRole;

/**
 * A DTO for the MoviePerson entity.
 */
public class MoviePersonDTO implements Serializable {

    private Long id;

    @NotNull
    private PersonRole role;

    private Long movieId;

    private String movieTitle;

    private Long personId;

    private String personName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PersonRole getRole() {
        return role;
    }

    public void setRole(PersonRole role) {
        this.role = role;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MoviePersonDTO moviePersonDTO = (MoviePersonDTO) o;
        if(moviePersonDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), moviePersonDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MoviePersonDTO{" +
            "id=" + getId() +
            ", role='" + getRole() + "'" +
            "}";
    }
}
