import java.util.ArrayList;
import java.util.HashMap;


public class Comptfidf {

	HashMap<Integer, Double> tfidf = new HashMap<>();
	ArrayList<Integer> indexes = new ArrayList<>(); 
	double mag =0;
	
	public void put(int key, double value){
		tfidf.put(key,value);
		indexes.add(key);
		mag+= Math.pow(value, 2);
	}
	
	public double get(int key){
		if(tfidf.containsKey(key))
			return tfidf.get(key);
		else
			System.out.println("EEEEEEEEEEEEEEEERRRRRRRRRRRRRRRRRRRRRROOOOOOOOOOOOOOOOOOOOORRRRRRRRRRR");
			return 0;
	}
	
	public double getMag(){
		return Math.sqrt(mag);
	}
}
