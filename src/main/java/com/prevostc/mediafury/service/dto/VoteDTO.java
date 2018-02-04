package com.prevostc.mediafury.service.dto;


import java.io.Serializable;
import java.time.Instant;
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

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
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
            ", createdBy=" + getCreatedBy() +
            ", createdDate=" + getCreatedDate() +
            ", lastModifiedBy='" + getLastModifiedBy() + '\'' +
            ", lastModifiedDate=" + getLastModifiedDate() +
            "}";
    }
}
