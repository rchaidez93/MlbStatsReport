package MlbStatsProject.rest.api;

import MlbStatsProject.mlbDTO.MlbReportDTO;
import MlbStatsProject.report.MlbReport;
import lombok.Data;

import java.util.List;

@Data
public class Response {

    private int status;
    private List<MlbReportDTO> report;
}
