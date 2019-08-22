package MlbStatsProject.report;

import MlbStatsProject.Mlb;
import MlbStatsProject.mlbDAO.MlbStatsDOAImpl;
import MlbStatsProject.mlbDTO.MlbStats;
import MlbStatsProject.tables.records.MlbStatsRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.Record;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MlbReport {

    private static final Logger logger = LogManager.getLogger(MlbReport.class);

    public MlbReport(int rowId) {
        //grab data from db
        MlbStatsDOAImpl mlbStatsDOA = new MlbStatsDOAImpl();
        List<MlbStats> mlbStats = mlbStatsDOA.getAll();

        this.run(mlbStats);
        this.endReport();
    }

    private void run(List<MlbStats> mlbStats) {
        // get metadata together
        mlbStats.forEach(x -> System.out.println(x));
    }


    private void endReport() {
        logger.info("Ending Program");
        System.exit(0);
    }

}
