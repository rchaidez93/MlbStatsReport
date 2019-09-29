package MlbStatsProject.mlbDTO;

import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Data
public class MlbStats {

    private int id;
    private long epoch_time;
    private int season;
    private String team1;
    private String team2;
    private int score1;
    private int score2;
    private String winner;

    public MlbStats(int id, long epoch_time, int season, String team1, String team2, int score1, int score2, String winner) {
        this.id = id;
        this.epoch_time = epoch_time;
        this.season = season;
        this.team1 = team1;
        this.team2 = team2;
        this.score1 = score1;
        this.score2 = score2;
        this.winner = winner;
    }

    public MlbStats(Map<String, String> row) {
        super();
        String tempEpoch = row.get("date");
        this.epoch_time = dateToEpochConverter(tempEpoch);
        this.season = Integer.parseInt(row.get("season"));
        this.team1 = row.get("team1");
        this.team2 = row.get("team2");;
        this.score1 = Integer.parseInt(row.get("score1"));
        this.score2 = Integer.parseInt(row.get("score2"));

        this.winner = (score1 > score2) ? team1 : team2;
    }

    private long dateToEpochConverter(String date){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        long epoch = 0;
        try {
            Date newDate = df.parse(date);
            epoch = newDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return epoch;
    }

}
