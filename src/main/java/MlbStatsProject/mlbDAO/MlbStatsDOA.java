package MlbStatsProject.mlbDAO;

import MlbStatsProject.MlbStats;

import java.util.List;

public interface MlbStatsDOA<T> {

    void update(T t);

    List<T> getAll();

    void save(List<MlbStats> mlbStatsList);

    void delete();
}
