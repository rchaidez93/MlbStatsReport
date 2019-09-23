package MlbStatsProject.rest;

import MlbStatsProject.Mlb;
import MlbStatsProject.MlbRunReport;
import MlbStatsProject.csv.CsvParser;
import MlbStatsProject.datasource.MyDataSourceFactory;
import MlbStatsProject.mlbDAO.MlbStatsDOA;
import MlbStatsProject.mlbDAO.MlbStatsDOAImpl;
import MlbStatsProject.mlbDTO.MlbReportDTO;
import MlbStatsProject.mlbDTO.MlbStats;
import MlbStatsProject.mlbReportQueueDAO.ReportQueueDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static MlbStatsProject.Tables.MLB_STATS;
import static MlbStatsProject.Tables.USERS;
import static org.jooq.impl.DSL.count;

@RestController
@RequestMapping("/api")
public class ReportController {

    private static final Logger logger = LogManager.getLogger(Mlb.class);
    private static DataSource ds = null;
    private static Connection con = null;

    /*
        TODO :
            Organize code,
            rest api responses,
            get report data structure react data grid needs
            chunking/server side rendering,
            update/delete/run_report api calls
    */
    @RequestMapping("/get_report")
    public List<MlbReportDTO> index() {

        MlbRunReport mlbRunReport = null;

        ds = MyDataSourceFactory.getMySQLDataSource();
        String command = "run_report";


        try {
            con = ds.getConnection();
            DSLContext create = DSL.using(con, SQLDialect.MYSQL_8_0);
            Result<Record1<Integer>> count = create.select(count(USERS.ID)).from(USERS).fetch();

            int mlbStatsRows = create.selectCount().from(MLB_STATS).fetchOne(0, int.class);

            MlbStatsDOA mlbstatsdoa = new MlbStatsDOAImpl();

            if(mlbStatsRows > 0){
//                    initReportTable();
                mlbRunReport = new MlbRunReport();
            }
            else{
                logger.error("No data found to run report");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }


        return mlbRunReport.getFullReport();
    }

    @RequestMapping("/update_report")
    public void update_report() throws IOException {
        CsvParser csvparser = new CsvParser();
        List<MlbStats> mlbStatsList = CsvParser.readcsv("mlb-elo/mlb_elo.csv");
        MlbStatsDOA mlbstatsdoa = new MlbStatsDOAImpl();
        mlbstatsdoa.delete();
        mlbstatsdoa.save(mlbStatsList);
        logger.error("Finished updating database");
    }

    @RequestMapping("/delete_stats")
    public void delete_report(){
        MlbStatsDOA mlbstatsdoa = new MlbStatsDOAImpl();
        mlbstatsdoa.delete();
        logger.info("Finished deleting all mlb stats data!");
    }


    //add new entry to mlb_report table if there is less than 3 already in queue
    private static void initReportTable() throws SQLException {
        ds = MyDataSourceFactory.getMySQLDataSource();
        con = ds.getConnection();
        DSLContext create = DSL.using(con, SQLDialect.MYSQL_8_0);

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