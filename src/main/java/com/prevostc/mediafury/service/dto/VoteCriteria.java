package com.prevostc.mediafury.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Vote entity. This class is used in VoteResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /votes?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class VoteCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private IntegerFilter winnerEloDiff;

    private IntegerFilter loserEloDiff;

    private LongFilter winnerId;

    private LongFilter loserId;

    public VoteCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getWinnerEloDiff() {
        return winnerEloDiff;
    }

    public void setWinnerEloDiff(IntegerFilter winnerEloDiff) {
        this.winnerEloDiff = winnerEloDiff;
    }

    public IntegerFilter getLoserEloDiff() {
        return loserEloDiff;
    }

    public void setLoserEloDiff(IntegerFilter loserEloDiff) {
        this.loserEloDiff = loserEloDiff;
    }

    public LongFilter getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(LongFilter winnerId) {
        this.winnerId = winnerId;
    }

    public LongFilter getLoserId() {
        return loserId;
    }

    public void setLoserId(LongFilter loserId) {
        this.loserId = loserId;
    }

    @Override
    public String toString() {
        return "VoteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (winnerEloDiff != null ? "winnerEloDiff=" + winnerEloDiff + ", " : "") +
                (loserEloDiff != null ? "loserEloDiff=" + loserEloDiff + ", " : "") +
                (winnerId != null ? "winnerId=" + winnerId + ", " : "") +
                (loserId != null ? "loserId=" + loserId + ", " : "") +
            "}";
    }

}
