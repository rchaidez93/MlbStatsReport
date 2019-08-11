package MlbStatsProject.csv;

import MlbStatsProject.MlbStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CsvParser {

    public CsvParser(){}

    public static List<MlbStats> readcsv(String FilePath) throws IOException {

        String DocumentsPath = System.getProperty("user.home") + "/Documents";
        Path documentsDirectory = Paths.get(DocumentsPath);
        Path csvPath = documentsDirectory.resolve(FilePath);

        CSVParser csvParser = CSVParser.parse(csvPath, Charset.defaultCharset(), CSVFormat.DEFAULT.withHeader("date", "season", "team1", "team2", "score1", "score2"));

        Stream<CSVRecord> csvRecordStream = StreamSupport.stream(csvParser.spliterator(), false);

        List<Map<String,String>> rowList = csvRecordStream
                .skip(1)
                .map(CSVRecord::toMap)
                .collect(Collectors.toList());

        List<MlbStats> mlbStatsList = rowList
                .stream()
                .map(row -> new MlbStats(row))
                .collect(Collectors.toList());

        return mlbStatsList;
    }
}
