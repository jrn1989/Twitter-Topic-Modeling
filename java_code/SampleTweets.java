package twitter.search;

import twitter4j.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.PreparedStatement;

import org.apache.commons.io.IOUtils;


public class SampleTweets {
	public static void main(String[] args) {

		Connection con = null;
		
		String url = "jdbc:mysql://localhost:3306/tweetsdb?useUnicode=yes&characterEncoding=utf-8";
		String user = "root";
		String password = "root";
		
		try {
			con = DriverManager.getConnection(url, user, password);
			final Statement st = con.createStatement();
			final PreparedStatement pst = con.prepareStatement("Insert into tws(Tid, UCreatedAt,UFavouritesCount,UFollowersCount,UFriendsCount,ULang,UListedCount,ULocation,UName,UScreenName,UTweetsCount,UTimeZone,UURL,UisGeoEnabled,TCreatedAt,TFavoriteCount,TGeoLocationLatitude,TGeoLocationLongitude,TIsoLanguageCode,TCountry,TCountryCo,TPlaceURL,TPlaceFullName,TPlaceId,TPlaceType,TStreetAddress,TPlaceName,TPlaceString,TRetweetCount,TSource,TText,TisFavorited,TisRetweet,TisRetweeted,TURL,TURLContent) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

			TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
			
			StatusListener listener = new StatusListener() {
				@Override
				public void onStatus(Status status) {
				
					try {
//						System.out.println(status.getId());
						pst.setLong(1, status.getId());
						pst.setLong(2, status.getUser().getCreatedAt().getTime());
						pst.setInt(3, status.getUser().getFavouritesCount());
						pst.setInt(4, status.getUser().getFollowersCount());
						pst.setInt(5, status.getUser().getFriendsCount());
						pst.setString(6, status.getUser().getLang());
						pst.setInt(7, status.getUser().getListedCount());
						pst.setString(8, status.getUser().getLocation());
						pst.setString(9, status.getUser().getName());
						pst.setString(10, status.getUser().getScreenName());
						pst.setInt(11, status.getUser().getStatusesCount());
						pst.setString(12, status.getUser().getTimeZone());
						pst.setString(13, status.getUser().getURL());
						pst.setBoolean(14, status.getUser().isGeoEnabled());
						
						pst.setLong(15, status.getCreatedAt().getTime());
						pst.setInt(16, status.getFavoriteCount());
						if(status.getGeoLocation()!=null)
						{
							pst.setDouble(17, status.getGeoLocation().getLatitude());
							pst.setDouble(18, status.getGeoLocation().getLongitude());
						}
						else {
							pst.setDouble(17, -1);
							pst.setDouble(18, -1);
						}
						
						if(status.getIsoLanguageCode().equals("en"))
							pst.setString(19, status.getIsoLanguageCode());
						else
							return;
						
						if(status.getPlace()!=null)
						{
							pst.setString(20, status.getPlace().getCountry());
							pst.setString(21, status.getPlace().getCountryCode());
							pst.setString(22, status.getPlace().getURL());
							pst.setString(23, status.getPlace().getFullName());
							pst.setString(24, status.getPlace().getId());
							pst.setString(25, status.getPlace().getPlaceType());
							pst.setString(26, status.getPlace().getStreetAddress());
							pst.setString(27, status.getPlace().getName());
							pst.setString(28, status.getPlace().toString());
						}
						else{
							return;
//							pst.setString(20, "");
//							pst.setString(21, "");
//							pst.setString(22, "");
//							pst.setString(23, "");
//							pst.setString(24, "");
//							pst.setString(25, "");
//							pst.setString(26, "");
//							pst.setString(27, "");
//							pst.setString(28, "");
						}
						pst.setInt(29, status.getRetweetCount());
						pst.setString(30, status.getSource());
						pst.setString(31, status.getText());
						pst.setBoolean(32, status.isFavorited());
						pst.setBoolean(33, status.isRetweet());
						pst.setBoolean(34, status.isRetweeted());
						
						
					
						URLEntity urls[] = status.getURLEntities();
						if(urls.length>0)
						{
							pst.setString(35,urls[0].getExpandedURL());
							try {
								pst.setString(36,IOUtils.toString( new URL( urls[0].getExpandedURL().toString())));
							} catch (Exception e) {
								pst.setString(36,"");
							}
					        
						}
						else
						{
							return;
//							pst.setString(35,"");
//							pst.setString(36,"");
						}
						
						pst.executeUpdate();
					} catch (SQLException e) {
						status.getText();
						e.printStackTrace();
					}
					
					System.gc();
					 
				}
				@Override
				public void onDeletionNotice(
						StatusDeletionNotice statusDeletionNotice) {

				}

				@Override
				public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

				}

				@Override
				public void onScrubGeo(long userId, long upToStatusId) {

				}

				@Override
				public void onStallWarning(StallWarning warning) {

				}

				@Override
				public void onException(Exception ex) {

				}
			};
			
			twitterStream.addListener(listener);
			twitterStream.sample();
		} catch (Exception ex) {
			Logger lgr = Logger.getLogger(Version.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
			
		}

	}
}
