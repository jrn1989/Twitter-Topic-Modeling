import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DocumentParser {
    private HashMap<String,Integer> termsDocsMaxfreq = new HashMap<>();
    private HashMap<String,Integer> allTerms = new HashMap<>();
    private Comptfidf[] tfidfDocsVector;
    private HashMap<String, Integer> freq = new HashMap<>();
   
    
    public void parseFiles(String filePath) throws FileNotFoundException, IOException {
        File[] allfiles = new File(filePath).listFiles();
        BufferedReader in = null;
        for (File f : allfiles) {
            if (f.getName().endsWith(".txt")) {
                in = new BufferedReader(new FileReader(f));
                StringBuilder sb = new StringBuilder();
                String s = null;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                }
                String[] tokenizedTerms = sb.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");
                int maxfreq=1;
                for (String term : tokenizedTerms) {
                	if(!freq.containsKey(f.getName()+term)){
                		int wfreq=1;
                		if (wfreq>maxfreq) maxfreq=wfreq;
                		freq.put(f.getName()+term, wfreq);
                		 if (!allTerms.containsKey(term)) {
                             allTerms.put(term,1);
                         }else{
                         	 allTerms.put(term,allTerms.get(term)+1);
                         }
                	}else{
                		int wfreq=freq.get(f.getName()+term)+1;
                		if (wfreq>maxfreq) maxfreq=wfreq;
                		freq.put(f.getName()+term, wfreq);
                	}
                   
                }
                termsDocsMaxfreq.put(f.getName(), maxfreq);
            }
        }

    }

    public void tfIdfCalculator() {
    	tfidfDocsVector= new Comptfidf[termsDocsMaxfreq.size()];
    	int docIndex=-1;
        for (String doc : termsDocsMaxfreq.keySet()) {
        	docIndex++;
        	Comptfidf tfidf= new Comptfidf();
            int index = -1;
            for (String term : allTerms.keySet()) {
            	index++;
            	if(freq.containsKey(doc+term)){
            		tfidf.put(index, ((double)freq.get(doc+term)/(double)termsDocsMaxfreq.get(doc)) * (Math.log((double)termsDocsMaxfreq.size()/(double)allTerms.get(term)+1)));
            	}
            }
            tfidfDocsVector[docIndex]= tfidf;
        }
    }

    public void getCosineSimilarity() throws FileNotFoundException {
    	PrintWriter out = new PrintWriter("F://USfinalrelation.txt");
    	int totalsize=tfidfDocsVector.length;
    	//double similarity[][] = new double[totalsize][totalsize]; 
        for (int i=0; i < totalsize; i++) {
        	//similarity[i][i]=1;
            for (int j=0; j < totalsize; j++) {
            	out.print(new CosineSimilarity().cosineSimilarity(tfidfDocsVector[i],tfidfDocsVector[j]));
            	out.print(";");
            	//similarity[i][j]=new CosineSimilarity().cosineSimilarity(tfidfDocsVector[i],tfidfDocsVector[j]);
            	//similarity[j][i]=similarity[i][j];
            }
            out.println();
            System.out.println(i);
        }
//        for (int i=0; i < totalsize; i++) {
//        	 for (int j=0; j < totalsize; j++) {
//             	out.print(similarity[i][j]);
//             	out.print(";");
//             }
//        	 out.println();
//        }
        out.flush();
    }
}