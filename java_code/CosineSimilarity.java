import java.util.ArrayList;

public class CosineSimilarity {

    public double cosineSimilarity(Comptfidf docVector1, Comptfidf docVector2) {
        double dotProduct = 0.0;
        double magnitude1 = docVector1.getMag();
        double magnitude2 = docVector2.getMag();
        double cosineSimilarity = 0.0;
        
        ArrayList<Integer> commonIndex = new ArrayList<>();
        int i=0,j=0;
        while (i<docVector1.indexes.size() && j<docVector2.indexes.size()){
        	if(docVector1.indexes.get(i).equals(docVector2.indexes.get(j))){
        		commonIndex.add(docVector1.indexes.get(i));
        		i++;j++;
        	}
        	else if(docVector1.indexes.get(i)<docVector2.indexes.get(j)){
        		i++;
        	}
        	else if(docVector1.indexes.get(i)>docVector2.indexes.get(j)){
        		j++;
        	}
        }
        //System.out.println("size:"+commonIndex.size());
        int offset;
        for (int index = 0; index < commonIndex.size(); index++){
        	offset=commonIndex.get(index);
            dotProduct += docVector1.get(offset) * docVector2.get(offset);
        }

        if (magnitude1 != 0.0 | magnitude2 != 0.0) {
            cosineSimilarity = dotProduct / (magnitude1 * magnitude2);
        } else {
            return 0.0;
        }
        return cosineSimilarity;
    }
}