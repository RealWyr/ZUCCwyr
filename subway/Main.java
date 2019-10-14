package subway;
import java.io.File;
import java.util.Scanner;
public class Main {
	 public static void main(String[] args) {
	        read();
	        System.out.println("����������ѯ");
	      while(true) {
	        write();
	        }
	    }
	    public static void read(){
	    	DistanceBuilder.FILE_PATH=System.getProperty("user.dir") + File.separator + "\\" +"subway.txt";
	    	DistanceBuilder.readSubway();
	    }
	    public static void write() {
	    	Scanner input=new Scanner(System.in);
	    	System.out.print("����ָ�(��ѯ������·��Ϣ��-info ��ͨ�ߣ�,����ѯ��ĩվ��·��-way star end��:");
	         String s=input.nextLine();
	         String[] split =s.split("\\s+");
	         if(split[0].equals("-map")) {
	        	 if(split.length==1){
	        		 DistanceBuilder.readSubway();
	                   System.out.println("�ɹ���ȡsubway.txt�ļ�");
	               }else{
	                   System.out.println("��������");
	               }
	         }
	         else if(split[0].equals("-info")) {
	              if(split.length==2){
	            	  DistanceBuilder.readSubway();
	            	  DistanceBuilder.writeLineData(split[1]);
	               }else{

	                   System.out.println("�����������������");
	               }
	         }
	         else if(split[0].equals("-way")) {
	               if(split.length==3){
	            	   if(split[1].equals(split[2]))
	            		   System.out.println("����վ���ظ�������������");
	            	   else {
	            	   DistanceBuilder.readSubway();
	            	   System.out.println(split[1]+" -> "+split[2]+":");
	                   Result result = DijkstraUtil.calculate(new Station(split[1]), new Station(split[2]));
	                   //System.out.println(split[2]);
	                   DistanceBuilder.getPassStation(result);
	                   //System.out.println(result);
	                   if(result.getDistance()==0.0)
	                	   System.out.println("����վ�㲻����");
	               }
	               }else{
	                   System.out.println("�����������������");
	               }
	               
	         
	         }
		    	System.out.println();
	    }
}