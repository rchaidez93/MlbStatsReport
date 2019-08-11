package MlbStatsProject;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.TimerTask;

public class MlbReport extends TimerTask {
//    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(RunReport.class);
    private static final Logger logger = LogManager.getLogger(Mlb.class);

    public MlbReport(){

    }

    @Override
    public void run(){
        logger.info("Running report now!");
        logger.info("Ending Program");
        System.exit(0);
    }
}
