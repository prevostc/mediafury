package com.prevostc.mediafury.service.elo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(Parameterized.class)
public class EloServiceUnitTest {

    private EloService eloService;

    private Integer winnerElo;
    private Integer loserElo;
    private Integer expectedWinnerElo;
    private Integer expectedLoserElo;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            // first game between same rank of 1000
            { 1000, 1000, 1016, 984 },
            // second game between same players
            { 1016, 984, 1031, 969 },
            // game won by high ranked, expect very little change
            { 2800, 1200, 2800, 1200 },
            { 1900, 1200, 1901, 1199 },
            { 1600, 1200, 1603, 1197 },
            // game won by low ranked, expect huge change
            { 1000, 2000, 1032, 1968 },
            { 1200, 2000, 1232, 1968 },
            { 1600, 2000, 1629, 1971 },
            { 1700, 1900, 1724, 1876 },
            // special values
            { 0, 0, 16, -16 },
            { -1000, -1000, -984, -1016 },
        });
    }

    public EloServiceUnitTest(Integer winnerElo, Integer loserElo, Integer expectedWinnerElo, Integer expectedLoserElo) {
        this.winnerElo = winnerElo;
        this.loserElo = loserElo;
        this.expectedWinnerElo = expectedWinnerElo;
        this.expectedLoserElo = expectedLoserElo;
    }

    @Before
    public void setup() {
        eloService = new EloService();
    }


    @Test
    public void testComputesEloProperly() {
        EloDTO oldElo = new EloDTO(winnerElo, loserElo);
        EloDTO newElo = eloService.computeElo(oldElo);

        assertThat(newElo.getWinnerElo()).isEqualTo(expectedWinnerElo);
        assertThat(newElo.getLoserElo()).isEqualTo(expectedLoserElo);
    }
}


