package MlbStatsProject;


import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import javax.swing.*;

import static MlbStatsProject.tables.MlbStats.MLB_STATS;
import static MlbStatsProject.tables.Users.USERS;
import static org.jooq.impl.DSL.count;

@SpringBootApplication
public class Mlb {

    private static final Logger logger = LogManager.getLogger(Mlb.class);
    private static DataSource ds = null;
    private static Connection con = null;


    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(Mlb.class, args);
    }
}
