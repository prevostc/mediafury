package com.prevostc.mediafury.service.dto;

import java.io.Serializable;
import com.prevostc.mediafury.domain.enumeration.PersonRole;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the MoviePerson entity. This class is used in MoviePersonResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /movie-people?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MoviePersonCriteria implements Serializable {
    /**
     * Class for filtering PersonRole
     */
    public static class PersonRoleFilter extends Filter<PersonRole> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private PersonRoleFilter role;

    private LongFilter movieId;

    private LongFilter personId;

    public MoviePersonCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public PersonRoleFilter getRole() {
        return role;
    }

    public void setRole(PersonRoleFilter role) {
        this.role = role;
    }

    public LongFilter getMovieId() {
        return movieId;
    }

    public void setMovieId(LongFilter movieId) {
        this.movieId = movieId;
    }

    public LongFilter getPersonId() {
        return personId;
    }

    public void setPersonId(LongFilter personId) {
        this.personId = personId;
    }

    @Override
    public String toString() {
        return "MoviePersonCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (role != null ? "role=" + role + ", " : "") +
                (movieId != null ? "movieId=" + movieId + ", " : "") +
                (personId != null ? "personId=" + personId + ", " : "") +
            "}";
    }

}
