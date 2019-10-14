package subway;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class DijkstraUtil {
    private static HashMap<Station, Result> resultMap = new HashMap<>();   // 分析过的站点集合.     
    private static List<Station> analysisList = new ArrayList<>();

    public static Result calculate(Station star, Station end) {   //迪杰斯特拉算法应用在地铁的实现.
        if (!analysisList.contains(star)) {
            analysisList.add(star);
        }
        if (star.equals(end)) {//起始站和终点站相同时返回result=0，终点站就是初始站
            Result result = new Result();
            result.setDistance(0.0D);
            result.setEnd(star);
            result.setStar(star);
            return resultMap.put(star, result);
            
        }
        if (resultMap.isEmpty()) {    //第一次调用calculate，且起始点和终止点不同，则resultMap为空。
            List<Station> linkStations = getLinkStations(star);   //第一次调用获取起始点的相邻站点（在所有地铁线中，这里涉及转线交叉的周围站点）
            for (Station station : linkStations) {   //把相邻站点集合中的所有站点，加入resultMap中，可以直接获取Distance。
                Result result = new Result();
                result.setStar(star);
                result.setEnd(station);
                String key = star.getName() + ":" + station.getName();
                Double distance = DistanceBuilder.getDistance(key);
                result.setDistance(distance);
                result.getPassStations().add(station);
                resultMap.put(station, result);
            }
        }
        Station parent = getNextStation();
        if (parent == null) {    //返回的parent为null，说明resultMap所有点keySet被分析完了
            Result result = new Result();
            result.setDistance(0.0D);
            result.setStar(star);
            result.setEnd(end);
            return resultMap.put(end, result);
        }
        if (parent.equals(end)) {   //寻找到终点，直接返回对应的result
            return resultMap.get(parent);
        }
        List<Station> childLinkStations = getLinkStations(parent);
        for (Station child : childLinkStations) {
            if (analysisList.contains(child)) {
                continue;
            }
            String key = parent.getName() + ":" + child.getName();
            Double distance;
            distance = DistanceBuilder.getDistance(key);
            DistanceBuilder.getDistance(key);
            if (parent.getName().equals(child.getName())) {
                distance = 0.0D;
            }
            Double parentDistance = resultMap.get(parent).getDistance();
            distance = doubleAdd(distance, parentDistance);
            List<Station> parentPassStations = resultMap.get(parent).getPassStations();
            Result childResult = resultMap.get(child);
            if (childResult != null) {
                if (childResult.getDistance() > distance) {     //通过最佳点比直接到距离小，就更新resultMap中的对应result对象。
                    childResult.setDistance(distance);
                    childResult.getPassStations().clear();
                    childResult.getPassStations().addAll(parentPassStations);//路线更新
                    childResult.getPassStations().add(child);
                }
            } else {   //通过最佳点比直接到距离小，直接从原路线到child点
                childResult = new Result();
                childResult.setDistance(distance);
                childResult.setStar(star);
                childResult.setEnd(child);
                childResult.getPassStations().addAll(parentPassStations);
                childResult.getPassStations().add(child);
            }
            resultMap.put(child, childResult);
        }
        analysisList.add(parent);
        calculate(star, end);
        return resultMap.get(end);
        
    }


    
     
    private static List<Station> getLinkStations(Station station) {  // 获取所有相邻节点.
        List<Station> linkedStaions = new ArrayList<Station>();
        for (List<Station> line : DistanceBuilder.lineSet) {    //遍历所有地铁线
            for (int i = 0; i < line.size(); i++) {
                if (station.equals(line.get(i))) {
                    if (i == 0) {
                        linkedStaions.add(line.get(i + 1));//地铁站为换乘线的初始站，则继续+1遍历
                    } else if (i == (line.size() - 1)) {
                        linkedStaions.add(line.get(i - 1));//地铁站为换乘线的终点站，则继续-1遍历
                    } else {                               //地铁站为换乘线的中途站，则继续+1，-1遍历
                        linkedStaions.add(line.get(i + 1));
                        linkedStaions.add(line.get(i - 1));
                    }
                }
            }
        }
        return linkedStaions;
    }

   
    private static Station getNextStation() {   //通过计算最小权值 计算下一个需要分析的点
        Double min = Double.MAX_VALUE;
        Station rets = null;
        Set<Station> stations = resultMap.keySet();
        for (Station station : stations) {
            if (analysisList.contains(station)) {   //跳过已经被分析过的站点
                continue;
            }
            Result result = resultMap.get(station);
            if (result.getDistance() < min) {   //寻找出未经过的最近点
                min = result.getDistance();
                rets = result.getEnd();
            }
        }
        return rets;
    }

 
    private static double doubleAdd(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }
}