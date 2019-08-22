package MlbStatsProject.mlbDTO;

import lombok.Data;

@Data
public class MlbReportDTO {

    private String team;
    private int wins;
    private int loses;
    private int highestScore;
    private int winStreak;
    private int loseStreak;
    private String year;

}
