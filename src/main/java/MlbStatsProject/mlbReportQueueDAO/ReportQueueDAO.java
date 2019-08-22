package MlbStatsProject.mlbReportQueueDAO;

import MlbStatsProject.datasource.MyDataSourceFactory;
import MlbStatsProject.tables.records.MlbReportRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static MlbStatsProject.Tables.MLB_REPORT;


public class ReportQueueDAO {

    private static final Logger logger = LogManager.getLogger(ReportQueueDAO.class);
    private static DataSource ds = null;
    private static Connection con = null;
    private DSLContext create;

    public ReportQueueDAO() throws SQLException {
        ds = MyDataSourceFactory.getMySQLDataSource();
        con = ds.getConnection();
        create = DSL.using(con, SQLDialect.MYSQL_8_0);
    }

    //add new report to queue
    public void insertReport(){
        int currentTimeMillis = (int) System.currentTimeMillis();
        MlbReportRecord mlbReportRecord = create.newRecord(MLB_REPORT);
        mlbReportRecord.setReportDate(UInteger.valueOf(currentTimeMillis));
        mlbReportRecord.setStatus(UInteger.valueOf(1));
        mlbReportRecord.setEmailSent(UByte.valueOf(0));
        mlbReportRecord.store();
        logger.info("MLb report entry created");
    }


    //get report id with status 1
    public Result<Record> getReportsStatus1(){
        Result<Record> rowCheck = create.select()
                .from(MLB_REPORT)
                .where(MLB_REPORT.STATUS.eq(UInteger.valueOf(1)))
                .fetch();

        return rowCheck;
    }

    //change to status 2 when report is finished.
    //change email status to 1 when report has been sent to email
    public void updateReportQueue(String column, int value, int rowId){

        Record record = create.newRecord(MLB_REPORT);
        if(column.matches("status")) record.set(MLB_REPORT.STATUS, UInteger.valueOf(value));
        if(column.matches("email")) record.set(MLB_REPORT.EMAIL_SENT, UByte.valueOf(value));

        create.update(MLB_REPORT)
                .set(record)
                .where(MLB_REPORT.ID.eq(rowId))
                .execute();
    }
}
