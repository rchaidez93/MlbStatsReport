package MlbStatsProject;


import MlbStatsProject.datasource.MyDataSourceFactory;
import MlbStatsProject.mlbDTO.MlbReportDTO;
import MlbStatsProject.mlbReportQueueDAO.ReportQueueDAO;
import MlbStatsProject.report.MlbReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static MlbStatsProject.Tables.MLB_REPORT;

public class MlbRunReport {
    private static final Logger logger = LogManager.getLogger(Mlb.class);
    private static DataSource ds = null;
    private static Connection con = null;
    MlbReport mlbReport = null;
    MlbReport fullReport = null;

    //want to run more than 1 report at once
    public MlbRunReport() throws SQLException {
        ds = MyDataSourceFactory.getMySQLDataSource();
        con = ds.getConnection();


        logger.info("Checking reports table for status 1!");
        DSLContext create = DSL.using(con, SQLDialect.MYSQL_8_0);
        ReportQueueDAO reportQueueDAO = null;
        Result<Record> rowCheck = null;
        try {
            reportQueueDAO = new ReportQueueDAO();
            //simple query to get the report(s) that has status 1
            rowCheck = reportQueueDAO.getReportsStatus1();
            if (rowCheck.size() > 0) {
                //change the status to 2 for report running indication and run the report.
                for (Record x : rowCheck) {//send to mlbReport and update the db
                    int rowId = x.getValue(MLB_REPORT.ID);
                    reportQueueDAO.updateReportQueue("status", 2, rowId);
                    fullReport = new MlbReport(rowId);
//                    fullReport = mlbReport.getFullReport();
                }
            }
            else{
                logger.info("No report to run at this time. Exiting program.");
                System.exit(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<MlbReportDTO> getFullReport() {
        return fullReport.getFullReport();
    }


}
