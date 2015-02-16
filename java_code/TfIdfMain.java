
import java.io.FileNotFoundException;
import java.io.IOException;

public class TfIdfMain {
    
    public static void main(String args[]) throws FileNotFoundException, IOException
    {
        DocumentParser dp = new DocumentParser();
        dp.parseFiles("F:\\cluster_US1");
        System.out.println("Done importing terms");
        dp.tfIdfCalculator();
        System.out.println("Done tfidf");
        dp.getCosineSimilarity();   
    }
}