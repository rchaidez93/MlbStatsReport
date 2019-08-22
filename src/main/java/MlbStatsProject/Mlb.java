package MlbStatsProject;


import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Timer;

import MlbStatsProject.csv.CsvParser;
import MlbStatsProject.datasource.MyDataSourceFactory;
import MlbStatsProject.mlbDAO.MlbStatsDOA;
import MlbStatsProject.mlbDAO.MlbStatsDOAImpl;
import MlbStatsProject.mlbDTO.MlbStats;
import MlbStatsProject.mlbReportQueueDAO.ReportQueueDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.*;
import org.jooq.impl.DSL;

import javax.sql.DataSource;

import static MlbStatsProject.tables.MlbStats.MLB_STATS;
import static MlbStatsProject.tables.Users.USERS;
import static org.jooq.impl.DSL.count;

public class Mlb {

    private static final Logger logger = LogManager.getLogger(Mlb.class);
    private static DataSource ds = null;
    private static Connection con = null;


    public static void main(String[] args) throws IOException, InterruptedException {
        ds = MyDataSourceFactory.getMySQLDataSource();
        String command = "run_report";
        if(args.length > 0){
            command = args[0];
        }

        try {
            con = ds.getConnection();
            DSLContext create = DSL.using(con, SQLDialect.MYSQL_8_0);
            Result<Record1<Integer>> count = create.select(count(USERS.ID)).from(USERS).fetch();

            int mlbStatsRows = create.selectCount().from(MLB_STATS).fetchOne(0, int.class);

            logger.error(System.currentTimeMillis());
            MlbStatsDOA mlbstatsdoa = new MlbStatsDOAImpl();
            if(command.matches("update_db")){
                CsvParser csvparser = new CsvParser();
                List<MlbStats> mlbStatsList = CsvParser.readcsv("mlb-elo/mlb_elo.csv");
                mlbstatsdoa.delete();
                mlbstatsdoa.save(mlbStatsList);
                logger.error("Finished updating database");
            }
            else if(command.matches("run_report")){
                if(mlbStatsRows > 0){
//                    initReportTable();
                    logger.info("Report is about to run!");
                    Timer t = new Timer();
                    MlbRunReport mlbRunReport = new MlbRunReport();
                    t.schedule(mlbRunReport, 100);
                }
                else{
                    logger.error("No data found to run report");
                }
            }
            else if(command.matches(("delete_stats"))){
                logger.info("Deleting all mlb stats data!");
                mlbstatsdoa.delete();
            }
            else{
                logger.error("Command not found. Enter either update_db, run_report, or delete_stats");
                System.exit(0);
            }


        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    //add new entry to mlb_report table if there is less than 3 already in queue
    private static void initReportTable() throws SQLException {
        ds = MyDataSourceFactory.getMySQLDataSource();
        con = ds.getConnection();
        DSLContext create = DSL.using(con, SQLDialect.MYSQL_8_0);

//        int rowCount = create.selectCount().from(MLB_STATS).fetchOne().value1();
        MlbStatsDOAImpl mlbStatsDOA = new MlbStatsDOAImpl();
        Record record = mlbStatsDOA.fetchOne();

        if(record.size() > 0){
            ReportQueueDAO reportQueueDAO = new ReportQueueDAO();
            reportQueueDAO.insertReport();
        }
        else{
            logger.info("Too many reports being ran. Try again in a few minutes.");
            System.exit(0);
        }


    }

}
