package subway;

import java.util.ArrayList;
import java.util.List;

public class Result {
    private Station star;
    private Station end;
    private Double distance = 0.0D;
    private List<Station> passStations = new ArrayList<>();
    public Station getStar() {
        return star;
    }
    public void setStar(Station star) {
        this.star = star;
    }
    public Station getEnd() {
        return end;
    }
    public void setEnd(Station end) {
        this.end = end;
    }
    public Double getDistance() {
        return distance;
    }
    public void setDistance(Double distance) {
        this.distance = distance;
    }
    public List<Station> getPassStations() {
        return passStations;
    }
    public void setPassStations(List<Station> passStations) {
        this.passStations = passStations;
    }
    public Result(Station star, Station end, Double distance) {
        this.star = star;
        this.end = end;
        this.distance = distance;
    }
    public Result() {

    }

    @Override
    public String toString() {
        return "Result{" +
                "star=" + star +
                ", end=" + end +
                ", distance=" + distance +
                ", passStations=" + passStations +
                '}';
    }
}