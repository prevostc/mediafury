package com.prevostc.mediafury.service.elo;

public class EloDTO {
    private Integer winnerElo;
    private Integer loserElo;

    public EloDTO(Integer winnerElo, Integer loserElo) {
        this.winnerElo = winnerElo;
        this.loserElo = loserElo;
    }

    public Integer getWinnerElo() {
        return winnerElo;
    }

    public Integer getLoserElo() {
        return loserElo;
    }
}
