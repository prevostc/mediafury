package com.prevostc.mediafury.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Vote.
 */
@Entity
@Table(name = "vote")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Vote extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "winner_elo_diff")
    private Integer winnerEloDiff;

    @Column(name = "loser_elo_diff")
    private Integer loserEloDiff;

    @ManyToOne(optional = false)
    @NotNull
    private Movie winner;

    @ManyToOne(optional = false)
    @NotNull
    private Movie loser;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWinnerEloDiff() {
        return winnerEloDiff;
    }

    public Vote winnerEloDiff(Integer winnerEloDiff) {
        this.winnerEloDiff = winnerEloDiff;
        return this;
    }

    public void setWinnerEloDiff(Integer winnerEloDiff) {
        this.winnerEloDiff = winnerEloDiff;
    }

    public Integer getLoserEloDiff() {
        return loserEloDiff;
    }

    public Vote loserEloDiff(Integer loserEloDiff) {
        this.loserEloDiff = loserEloDiff;
        return this;
    }

    public void setLoserEloDiff(Integer loserEloDiff) {
        this.loserEloDiff = loserEloDiff;
    }

    public Movie getWinner() {
        return winner;
    }

    public Vote winner(Movie movie) {
        this.winner = movie;
        return this;
    }

    public void setWinner(Movie movie) {
        this.winner = movie;
    }

    public Movie getLoser() {
        return loser;
    }

    public Vote loser(Movie movie) {
        this.loser = movie;
        return this;
    }

    public void setLoser(Movie movie) {
        this.loser = movie;
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
        Vote vote = (Vote) o;
        if (vote.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vote.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Vote{" +
            "id=" + getId() +
            ", winnerEloDiff=" + getWinnerEloDiff() +
            ", loserEloDiff=" + getLoserEloDiff() +
            "}";
    }
}
