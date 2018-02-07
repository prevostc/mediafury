package com.prevostc.mediafury.service.elo;


import org.springframework.stereotype.Service;

@Service
public class EloService {

    /**
     * Knob K to control how much to impact ratings
     * Players lose or gain at most this amount
     * Players with equal ELO will respectively gain and lose K/2 points
     */
    private final Integer ELO_IMPACT_FACTOR = 32;

    /**
     * Compute new ELO rating based on current ELO rating
     * @param dto the current ELO ratings
     * @return the new ELO ratings
     */
    public EloDTO computeElo(EloDTO dto) {

        // step 1 - transform: R = 10^(elo/400)
        double winR = Math.pow(10, dto.getWinnerElo()/400.0d);
        double losR = Math.pow(10, dto.getLoserElo()/400.0d);

        // step 2 - expected: E = R / (R1 + R2)
        double winE = winR / (winR + losR);
        double losE = losR / (winR + losR);

        // step 3 - success: S = 1 if win, 0 if lost
        // step 4 - impact: r = r + K * (S – E)
        double winRating = dto.getWinnerElo() + ELO_IMPACT_FACTOR * (1 - winE);
        double losRating = dto.getLoserElo() + ELO_IMPACT_FACTOR * (0 - losE);

        return new EloDTO(
            Math.toIntExact(Math.round(winRating)),
            Math.toIntExact(Math.round(losRating))
        );
    }
}

