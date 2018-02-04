package com.prevostc.mediafury.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Vote entity.
 */
public class VoteDTO implements Serializable {

    private Long id;

    private Integer winnerEloDiff;

    private Integer loserEloDiff;

    private Long winnerId;

    private String winnerTitle;

    private Long loserId;

    private String loserTitle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWinnerEloDiff() {
        return winnerEloDiff;
    }

    public void setWinnerEloDiff(Integer winnerEloDiff) {
        this.winnerEloDiff = winnerEloDiff;
    }

    public Integer getLoserEloDiff() {
        return loserEloDiff;
    }

    public void setLoserEloDiff(Integer loserEloDiff) {
        this.loserEloDiff = loserEloDiff;
    }

    public Long getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(Long movieId) {
        this.winnerId = movieId;
    }

    public String getWinnerTitle() {
        return winnerTitle;
    }

    public void setWinnerTitle(String movieTitle) {
        this.winnerTitle = movieTitle;
    }

    public Long getLoserId() {
        return loserId;
    }

    public void setLoserId(Long movieId) {
        this.loserId = movieId;
    }

    public String getLoserTitle() {
        return loserTitle;
    }

    public void setLoserTitle(String movieTitle) {
        this.loserTitle = movieTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VoteDTO voteDTO = (VoteDTO) o;
        if(voteDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), voteDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VoteDTO{" +
            "id=" + getId() +
            ", winnerEloDiff=" + getWinnerEloDiff() +
            ", loserEloDiff=" + getLoserEloDiff() +
            "}";
    }
}
