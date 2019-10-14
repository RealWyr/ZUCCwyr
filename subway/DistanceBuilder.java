package subway;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class DistanceBuilder {
    public  static String FILE_PATH;
    public  static String WRITE_PATH;
    public static HashMap<String,HashMap<String,Double>> distanceMap = new HashMap<String,HashMap<String,Double>>();
    public static LinkedHashSet<List<Station>> lineSet = new LinkedHashSet<>();//所有线集合
    public static HashMap<String,List<Station>> lineData;
    private DistanceBuilder() {
    }

    static {
        createlineData();
    }
    public static void createlineData(){
        lineData = new HashMap<>();
        for (List<Station> stations : lineSet) {
            lineData.put(stations.get(1).getLine(),stations);
        }
    }
    public static String getLineNameByStation(Station station){
        createlineData();
        String startname = station.getName();
        for (Map.Entry<String,List<Station>> entry : lineData.entrySet()) {
            List<Station> stations =  entry.getValue();
            for (Station sta : stations){
                if(sta.getName().equals(startname)){
                    return entry.getKey();
                }
            }
        }
        return "";
    }
    //传入 AA:BB 通过遍历每条线，在每条线中找AA:BB
    public static Double getDistance(String key) {
        return distanceMap.entrySet().stream().filter(x->x.getValue().keySet().contains(key)).findFirst().get().getValue().get(key);
    }
    //key格式为：“ 苹果园:古城 ”
    public static String getLineName(String key) {
        //如果某地铁线包含该key，则返回地铁名。 findFirst返回Option<String> 其中String为地铁线名。
        return distanceMap.keySet().stream().filter(x -> distanceMap.get(x).containsKey(key)).findFirst().orElse("");
    }
    public static boolean isContains(String key){
        return distanceMap.entrySet().stream().anyMatch(x->x.getValue().keySet().contains(key));
    }
    public static ArrayList<Station> getLine(String lineStr,String lineName){
        ArrayList<Station> line =  new ArrayList<Station>();
        String[] lineArr = lineStr.split(",");
        for (String s : lineArr) {
            line.add(new Station(s,lineName));
        }
        return line;
    }

    public static void writeLineData(String lineName){
        createlineData();
        lineName = lineName.substring(0,3);
        List<Station> lineInfo = lineData.get(lineName);
        //System.out.println(lineData.get(lineName));
        String lineStr = lineInfo.stream().map(x->x.getName()).collect(Collectors.joining(",","[","]"));
        System.out.print(lineStr);
    }

    public static void getPassStation(Result result){
    	//System.out.print(result.getStar());
        String starLine = getLineNameByStation(result.getStar());
        String converLine = starLine;
        System.out.println("起始地铁线："+starLine);
        for (Station station : result.getPassStations()) {
            if(!converLine.equals(station.getLine())){
                System.out.print("换乘地铁线："+station.getLine()+"  ");
                converLine = station.getLine();
                converLine = station.getLine();
            }
            System.out.print(station.getName() + " > ");
        }
    }
    public static void readSubway() {
        File file = new File(FILE_PATH);
        BufferedReader reader = null;

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file),"UTF-8");

            reader = new BufferedReader(inputStreamReader);
            String line = null;
            //String如：苹果园:古城   double为距离，如：1
            //默认起始为1号线
            String lineName = "1";
            distanceMap.put("1",new HashMap<>());
            while ((line = reader.readLine()) != null) {
                //长度为1或2，则该行值为地铁线号。
                if(line.trim().length()==1||line.trim().length()==3||line.trim().length()==2){
                    if(line.trim().length()==3||line.trim().length()==2){ 
                        continue;
                    }
                    lineName = line;
                    //判断distanceMap中是否已创建该地铁线号。如果为创建该地铁线则加入distanceMap
                    if(!distanceMap.keySet().contains(line)){
                        distanceMap.put(line.trim(),new HashMap<>());
                    }
                }else{
                    //判断是否以"*"开头，如果*开头则为某条线的所有站点字符串。
                    if(line.trim().startsWith("*")){
                        String[] lineInfo = line.substring(1).split("-");
                        lineSet.add(getLine(lineInfo[1].trim(),lineInfo[0].trim()));
                    }else{
                        //地铁某站点到某站点信息，如:苹果园:古城	1
                        String texts[] = line.split("\t");
                        String key = texts[0].trim();
                        Double value = Double.valueOf(texts[1]);
                        distanceMap.get(lineName).put(key,value);
                        String other = key.split(":")[1].trim()+":"+key.split(":")[0].trim();
                        distanceMap.get(lineName).put(other,value);
                    }
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
        }


    }
}