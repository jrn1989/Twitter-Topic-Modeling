package twitter.search;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class tweetsTofiles {

	public static void main(String[] args) {
		Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        String url = "jdbc:mysql://localhost:3306/tweetsdb?useUnicode=yes&characterEncoding=utf-8";
		String user = "root";
		String password = "root";

		int docid=0;
        try {
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            PrintWriter printWriter = null;
            for(int i=1;i<=45000;i++)
            {
            rs = st.executeQuery("select t.Tid, t.TCountryCo, t.TText from tws t Limit "+i+" , "+1+";");
            //i+=100;
	         while (rs.next()) {
	            	docid++;
	            	printWriter = new PrintWriter("tweets//"+rs.getString(2)+"//"+rs.getBigDecimal(1)+".txt","UTF-8");
	            	 printWriter.println(rs.getString(3));
	            	 printWriter.flush();
	            	 printWriter.close();
	            }
            }

        } catch (Exception ex) {
        	System.out.println(ex);

        } finally {

        }
	}

}
