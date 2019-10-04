package MlbStatsProject.rest;

import MlbStatsProject.Mlb;
import MlbStatsProject.MlbRunReport;
import MlbStatsProject.csv.CsvParser;
import MlbStatsProject.datasource.MyDataSourceFactory;
import MlbStatsProject.mlbDAO.MlbStatsDOA;
import MlbStatsProject.mlbDAO.MlbStatsDOAImpl;
import MlbStatsProject.mlbDTO.MlbReportDTO;
import MlbStatsProject.mlbDTO.MlbStats;
import MlbStatsProject.rest.api.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.types.UInteger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
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

        logger.info("Sending report");
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

    @RequestMapping("/tellmeabout")
    public ResponseEntity tellMeAbout(@RequestParam String name){

        ds = MyDataSourceFactory.getMySQLDataSource();
        String response = "";
        Response apiResponse = new Response();
        apiResponse.setStatus(0);
        apiResponse.setMessage("Error");

        try {
            con = ds.getConnection();
            DSLContext create = DSL.using(con, SQLDialect.MYSQL_8_0);
            System.out.println(name);
            Result<Record> user = create.select().from(USERS).where(USERS.FIRST_NAME.eq(name)).fetch();
            apiResponse.setStatus(1);
            apiResponse.setMessage(user.getValues(USERS.FIRST_NAME).get(0) + " " + user.getValues(USERS.LAST_NAME).get(0));
        }catch (Exception e) {
            e.printStackTrace();
        }


        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }


}