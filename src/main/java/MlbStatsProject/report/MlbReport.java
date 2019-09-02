package MlbStatsProject.report;

import MlbStatsProject.mlbDAO.MlbStatsDOAImpl;
import MlbStatsProject.mlbDTO.MlbReportDTO;
import MlbStatsProject.mlbDTO.MlbStats;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class MlbReport {

    private static final Logger logger = LogManager.getLogger(MlbReport.class);
    private int reportid;
    List<MlbReportDTO> reportData = new ArrayList<MlbReportDTO>();

    // TODO : send email, update db, end report
    public MlbReport(int rowId) {
        reportid = rowId;
        //grab data from db
        MlbStatsDOAImpl mlbStatsDOA = new MlbStatsDOAImpl();
        List<MlbStats> mlbStats = mlbStatsDOA.getAll();

        getReport(mlbStats);
        sendEmail();
        endReport();
    }

    // Columns: Year, Team, Wins, Winning Streak, Highest Score, Loses, Loosing Streak,
    // get metadata together
    private void getReport(List<MlbStats> mlbStats) {

        HashSet<String> allTeams = getAllTeams(mlbStats);
        List<Integer> allYears = getAllYears(mlbStats);

        Map<Integer, String> yearTeam= new HashMap<>();
        allYears.forEach(x -> {
            allTeams.forEach(y -> {
                MlbReportDTO reportDTO = new MlbReportDTO();
                reportDTO.setYear(x);
                reportDTO.setTeam(y);
                reportDTO.setWins(getWins(x,y,mlbStats));
                reportDTO.setLoses(getLoses(x,y,mlbStats));
                reportDTO.setWinStreak(getWinningStreak(x,y,mlbStats));
                reportDTO.setLoosingStreak(getLoosingStreak(x,y,mlbStats));
                reportDTO.setHighestScore(getHighestScore(x,y,mlbStats));
                reportData.add(reportDTO);
            });
        });

        System.out.println(reportData);
    }

    private int getLoosingStreak(Integer year, String team, List<MlbStats> mlbStats) {
        AtomicInteger loosingStreak = new AtomicInteger();
        AtomicInteger newLoosingStreak = new AtomicInteger();

        mlbStats.stream()
                .filter(mlb -> {
                    return  ((mlb.getSeason() == 2019) && (mlb.getTeam1().matches("STL") || mlb.getTeam2().matches("STL")) );
                })
                .forEach(mlb -> {
                    if(!mlb.getWinner().matches(team)){
                        loosingStreak.incrementAndGet();
                        if(loosingStreak.get() > newLoosingStreak.get()){
                            newLoosingStreak.set(loosingStreak.get());
                        }
                    }
                    else{
                        loosingStreak.set(0);
                    }
                });

        loosingStreak.set(0);
        return Integer.parseInt(String.valueOf(newLoosingStreak));
    }

    private int getLoses(int year, String team, List<MlbStats> mlbStats) {
        AtomicInteger loses = new AtomicInteger();

        mlbStats.stream()
                .filter(mlb -> {
                    return  ((mlb.getSeason() == year) && (mlb.getTeam1().matches(team) || mlb.getTeam2().matches(team)) );
                })
                .forEach(mlb -> {
                    if(mlb.getTeam1().matches(team)){
                        if(mlb.getScore1() < mlb.getScore2()){
                            loses.incrementAndGet();
                        }
                    }
                    else{
                        if(mlb.getScore2() < mlb.getScore1()){
                            loses.incrementAndGet();
                        }
                    }
                });

        return Integer.parseInt(String.valueOf(loses));
    }

    private int getHighestScore(Integer year, String team, List<MlbStats> mlbStats) {
        AtomicInteger highestScore = new AtomicInteger();
        AtomicInteger newHighestScore = new AtomicInteger();

        mlbStats.stream()
                .filter(mlb -> {
                    return  ((mlb.getSeason() == 2019) && (mlb.getTeam1().matches("STL") || mlb.getTeam2().matches("STL")) );
                })
                .forEach(mlb -> {
                    if(mlb.getTeam1().matches(team)){
                        highestScore.set(mlb.getScore1());
                        if(highestScore.get() > newHighestScore.get()){
                            newHighestScore.set(highestScore.get());
                        }
                    }
                    else{
                        highestScore.set(mlb.getScore2());
                        if(highestScore.get() > newHighestScore.get()){
                            newHighestScore.set(highestScore.get());
                        }
                    }
                });

        return Integer.parseInt(String.valueOf(newHighestScore));
    }

    private int getWinningStreak(int year, String team, List<MlbStats> mlbStats) {
        AtomicInteger winningStreak = new AtomicInteger();
        AtomicInteger newWinningStreak = new AtomicInteger();

        mlbStats.stream()
                .filter(mlb -> {
                    return  ((mlb.getSeason() == year) && (mlb.getTeam1().matches(team) || mlb.getTeam2().matches(team)) );
                })
                .forEach(mlb -> {
                    if(mlb.getWinner().matches(team)){
                        winningStreak.incrementAndGet();
                        if(winningStreak.get() > newWinningStreak.get()){
                            newWinningStreak.set(winningStreak.get());
                        }
                    }
                    else{
                        winningStreak.set(0);
                    }
                });

        winningStreak.set(0);
        return Integer.parseInt(String.valueOf(newWinningStreak));
    }

    private int getWins(Integer year, String team, List<MlbStats> mlbStats) {

        AtomicInteger wins = new AtomicInteger();
        mlbStats.stream().
                filter(c -> {
                    return  ((c.getSeason() == year) && (c.getWinner().matches(team)));
                })
                .forEach(c -> {
                    wins.incrementAndGet();
                });

        return Integer.parseInt(String.valueOf(wins));
    }

    //get unique teams and year
    private HashSet<String> getAllTeams(List<MlbStats> mlbStats) {

        HashMap<Integer,String> distinctTeams1 = (HashMap<Integer, String>) mlbStats.stream()
                .distinct()
                .collect(Collectors.toMap(MlbStats::getId,MlbStats::getTeam1));

        HashMap<Integer, String> distinctTeams2 = (HashMap<Integer, String>) mlbStats.stream()
                .distinct()
                .collect(toMap(MlbStats::getId,MlbStats::getTeam1));

        distinctTeams1.putAll(distinctTeams2);

        HashSet<String> allDistinctTeams = new HashSet<String>();
        allDistinctTeams.addAll(distinctTeams1.values());


        return allDistinctTeams;
    }

    private List<Integer> getAllYears(List<MlbStats> mlbStats){

        List<Integer> years = new ArrayList<>();
        mlbStats.forEach(x-> {
            years.add(x.getSeason());
        });

        List<Integer> allYears = years.stream()
                .distinct()
                .collect(toList());

        return allYears;
    }


    private void endReport() {
        logger.info("Ending Program");
        System.exit(0);
    }

    private void sendEmail() {
    }


}
