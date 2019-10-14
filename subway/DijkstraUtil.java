package subway;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class DijkstraUtil {
    private static HashMap<Station, Result> resultMap = new HashMap<>();   // ��������վ�㼯��.     
    private static List<Station> analysisList = new ArrayList<>();

    public static Result calculate(Station star, Station end) {   //�Ͻ�˹�����㷨Ӧ���ڵ�����ʵ��.
        if (!analysisList.contains(star)) {
            analysisList.add(star);
        }
        if (star.equals(end)) {//��ʼվ���յ�վ��ͬʱ����result=0���յ�վ���ǳ�ʼվ
            Result result = new Result();
            result.setDistance(0.0D);
            result.setEnd(star);
            result.setStar(star);
            return resultMap.put(star, result);
            
        }
        if (resultMap.isEmpty()) {    //��һ�ε���calculate������ʼ�����ֹ�㲻ͬ����resultMapΪ�ա�
            List<Station> linkStations = getLinkStations(star);   //��һ�ε��û�ȡ��ʼ�������վ�㣨�����е������У������漰ת�߽������Χվ�㣩
            for (Station station : linkStations) {   //������վ�㼯���е�����վ�㣬����resultMap�У�����ֱ�ӻ�ȡDistance��
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
        if (parent == null) {    //���ص�parentΪnull��˵��resultMap���е�keySet����������
            Result result = new Result();
            result.setDistance(0.0D);
            result.setStar(star);
            result.setEnd(end);
            return resultMap.put(end, result);
        }
        if (parent.equals(end)) {   //Ѱ�ҵ��յ㣬ֱ�ӷ��ض�Ӧ��result
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
                if (childResult.getDistance() > distance) {     //ͨ����ѵ��ֱ�ӵ�����С���͸���resultMap�еĶ�Ӧresult����
                    childResult.setDistance(distance);
                    childResult.getPassStations().clear();
                    childResult.getPassStations().addAll(parentPassStations);//·�߸���
                    childResult.getPassStations().add(child);
                }
            } else {   //ͨ����ѵ��ֱ�ӵ�����С��ֱ�Ӵ�ԭ·�ߵ�child��
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


    
     
    private static List<Station> getLinkStations(Station station) {  // ��ȡ�������ڽڵ�.
        List<Station> linkedStaions = new ArrayList<Station>();
        for (List<Station> line : DistanceBuilder.lineSet) {    //�������е�����
            for (int i = 0; i < line.size(); i++) {
                if (station.equals(line.get(i))) {
                    if (i == 0) {
                        linkedStaions.add(line.get(i + 1));//����վΪ�����ߵĳ�ʼվ�������+1����
                    } else if (i == (line.size() - 1)) {
                        linkedStaions.add(line.get(i - 1));//����վΪ�����ߵ��յ�վ�������-1����
                    } else {                               //����վΪ�����ߵ���;վ�������+1��-1����
                        linkedStaions.add(line.get(i + 1));
                        linkedStaions.add(line.get(i - 1));
                    }
                }
            }
        }
        return linkedStaions;
    }

   
    private static Station getNextStation() {   //ͨ��������СȨֵ ������һ����Ҫ�����ĵ�
        Double min = Double.MAX_VALUE;
        Station rets = null;
        Set<Station> stations = resultMap.keySet();
        for (Station station : stations) {
            if (analysisList.contains(station)) {   //�����Ѿ�����������վ��
                continue;
            }
            Result result = resultMap.get(station);
            if (result.getDistance() < min) {   //Ѱ�ҳ�δ�����������
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