README

This readme file is to describe the pupose of each java file/class

classes that contain main method

SampleTweets.java: is to add a listener to twitter sampler, each time a tweet is been added this class is called.
					the class will take the tweet and its properties, and add it to the database, also the same class will parse the url and get its content

urlcontentTofile: simple java class to take the tweet shared url content from the database and saved it to html file named with the tweet id
tweetsTofile: takes retrieve the text of tweets from the database and save it to files foldered by the country code, so each country code has a folder contains its tweets.
TfidfMain.java: takes a files of tweets and calculate the tfidf for each tweet then calculate the similarity measure between those tweets


classes that are called by other (all used by TfidfMain)

CosineSimilarity.java: used to calculate the cosine difference between two tfidf vector
TfIdf.java, comptfidf.java: used to calculate the tfidf of a tweet from a collection of tweets
DocumentParser.java: used to parse the tweets and calculate the different frequencies

