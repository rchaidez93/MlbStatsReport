package MlbStatsProject.mlbDTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class MlbReportDTO {

    private String team;
    private int wins;
    private int loses;
    private int highestScore;
    private int winStreak;
    private int loosingStreak;
    private long year;

}
