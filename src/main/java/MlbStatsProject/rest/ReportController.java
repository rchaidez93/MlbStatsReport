package MlbStatsProject.rest;

import MlbStatsProject.Mlb;
import MlbStatsProject.report.MlbRunReport;
import MlbStatsProject.csv.CsvParser;
import MlbStatsProject.datasource.MyDataSourceFactory;
import MlbStatsProject.mlbDAO.MlbStatsDOA;
import MlbStatsProject.mlbDAO.MlbStatsDOAImpl;
import MlbStatsProject.mlbDTO.MlbStats;
import MlbStatsProject.rest.api.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import static MlbStatsProject.Tables.MLB_STATS;

@Controller
public class ReportController {

    private static final Logger logger = LogManager.getLogger(Mlb.class);
    private static DataSource ds = null;
    private static Connection con = null;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/get_report")
    public ResponseEntity get_report() {
        MlbRunReport mlbRunReport = null;
        ds = MyDataSourceFactory.getMySQLDataSource();

        try {
            con = ds.getConnection();
            DSLContext create = DSL.using(con, SQLDialect.MYSQL_8_0);
            int mlbStatsRows = create.selectCount().from(MLB_STATS).fetchOne(0, int.class);

            if(mlbStatsRows > 0){
                mlbRunReport = new MlbRunReport();
            }
            else{
                logger.error("No data found to run report");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        Response response = new Response();
        response.setStatus(1);
        response.setReport(mlbRunReport.getFullReport());

        return new ResponseEntity(response, HttpStatus.OK);
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


}