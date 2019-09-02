package MlbStatsProject.mlbDTO;

import lombok.Data;

@Data
public class MlbReportDTO {

    private String team;
    private int wins;
    private int loses;
    private int highestScore;
    private int winStreak;
    private int loosingStreak;
    private long year;



    public String getTeam() {
        return team;
    }

    public int getWins() {
        return wins;
    }

    public int getLoses() {
        return loses;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public int getWinStreak() {
        return winStreak;
    }

    public int getLoosingStreak() {
        return loosingStreak;
    }

    public long getYear() {
        return year;
    }


    public void setTeam(String team) {
        this.team = team;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public void setWinStreak(int winStreak) {
        this.winStreak = winStreak;
    }

    public void setLoosingStreak(int loosingStreak) {
        this.loosingStreak = loosingStreak;
    }

    public void setYear(long year) {
        this.year = year;
    }

}
