package MlbStatsProject.mlbDAO;

import MlbStatsProject.mlbDTO.MlbStats;
import org.jooq.Record;

import java.util.List;

public interface MlbStatsDOA<T> {

    List<T> getAll();

    void save(List<MlbStats> mlbStatsList);

    void delete();

    Record fetchOne();
}
