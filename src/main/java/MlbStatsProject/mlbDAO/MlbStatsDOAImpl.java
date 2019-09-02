package MlbStatsProject.mlbDAO;

import MlbStatsProject.datasource.MyDataSourceFactory;
import MlbStatsProject.mlbDTO.MlbStats;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static MlbStatsProject.tables.MlbStats.MLB_STATS;

public class MlbStatsDOAImpl implements MlbStatsDOA<MlbStats> {

    private static DataSource ds = MyDataSourceFactory.getMySQLDataSource();

    private static Connection con = null;


    @Override
    public List<MlbStats> getAll() {

        List<MlbStats> mlbStatsRecord = null;
        try {
            con = ds.getConnection();
            DSLContext create = DSL.using(con, SQLDialect.MYSQL_8_0);
            mlbStatsRecord = create
                    .select()
                    .from(MLB_STATS)
                    .orderBy(MLB_STATS.EPOCH_DATE.desc())
                    .fetchInto(MlbStats.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mlbStatsRecord;
    }

    @Override
    public void save(List<MlbStats> mlbStatsList) {

        try {
            con = ds.getConnection();
            DSLContext create = DSL.using(con, SQLDialect.MYSQL_8_0);

            for (MlbStats mlbStats : mlbStatsList) {
                create.insertInto(MLB_STATS)
                        .set(MLB_STATS.EPOCH_DATE,mlbStats.getEpoch_time())
                        .set(MLB_STATS.SEASON, mlbStats.getSeason())
                        .set(MLB_STATS.TEAM1,mlbStats.getTeam1())
                        .set(MLB_STATS.TEAM2,mlbStats.getTeam2())
                        .set(MLB_STATS.SCORE1,mlbStats.getScore1())
                        .set(MLB_STATS.SCORE2, mlbStats.getScore2())
                        .set(MLB_STATS.WINNER, mlbStats.getWinner())
                        .execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete() {
        try {
            con = ds.getConnection();
            DSLContext create = DSL.using(con, SQLDialect.MYSQL_8_0);
            create.delete(MLB_STATS)
                    .execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Record fetchOne() {
        Record record = null;
        try {
            con = ds.getConnection();
            DSLContext create = DSL.using(con, SQLDialect.MYSQL_8_0);
            record = create.select()
                    .from(MLB_STATS)
                    .limit(1)
                    .fetchOne();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return record;
    }
}
