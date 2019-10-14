package subway;
import java.util.ArrayList;
import java.util.List;


public class Station {
  
    private String name;  
    private String line;
    private List<Station> linkStations = new ArrayList<>();
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLine() {
        return line;
    }
    public void setLine(String line) {
        this.line = line;
    }
    public List<Station> getLinkStations() {
        return linkStations;
    }
    public void setLinkStations(List<Station> linkStations) {
        this.linkStations = linkStations;
    }
    public Station(String name, String line) {
        this.name = name;
        this.line = line;
    }
    public Station(String name) {
        this.name = name;
    }
    public Station (){

    }
    public int hashCode() {
		return this.getName().hashCode();
	}
	public String toString() {
		return "Station{" + "name='" + name +'\'' +",line='" + line + '\'' + ", linkStations=" + linkStations + '}';
	}
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        } else if(obj instanceof Station){
            Station s = (Station) obj;
            if(s.getName().equals(this.getName())){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}