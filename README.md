# Twitter-Topic-Modeling
By: Jose Robles, Ahmed Abdelwahab

## What is this about?

In this project we would like to exploit the concealed potential behind tweets, 
specifically by identifying what are the hot topics that users, in different countries, talk about. We expect 
to make assumptions and maybe predictions about users' behaviour. 
The main focus will be in tweets and links written in English for such task.

## Tools and Technologies

The architecture of this project is the following:
![](https://github.com/jrn1989/Twitter-Topic-Modeling/blob/master/architecture.png )

### Data Extraction

The tweets are retrieved using the Java library Twitter4J 3.0 for the Twitter API and storing them in MySQL 5.6.
We decided to gather two datasets: The first dataset has been constructed by retrieving in real time all the information that can be found in a tweet 1. Over the period of 1 week, we got around 700,000 tweets. We decided to do this because we want to calculate some statistics about how many tweets are written in english for different countries, how many tweets contain the location and how many tweets have a shared URL. Filtering to those tweets that are written in English, have a shared URL and the location for the tweet has been attached we have X tweets.
The second dataset is quite similar to the one we constructed before, the only change we made was to apply the filters (tweets written in english, with URL content and the location attached) before being stored in the database. So far we have 6000 tweets in this second dataset.
Our idea is to perform Latent Dirichlet Allocation (LDA) Algorithm in the second dataset (let’s remember that the first one was only used to calculate some statistics) and compare each topic distribution to see if there is a difference in what people talk and share.

### Pre-processing

This part would correspond mainly to the tokenization of our documents. The tasks will included:

* Extract meta-information. Since we are interested in the content of the URL that has been shared, we must take into account that we are retrieving raw HTML files from those links. So we must remove all the markup format in order to have nice plain text that can be analysed by LDA. This step can be done by a simple HTML parsing that detects all headers, scripts, styles tags, etc. and just removes them. The text from the tweet won’t require such thing since it can be obtain from one of the database fields.
* Case folding. Reducing all the letters to lower case might be a good idea in order to have a more homogeneous vocabulary in our documents. On the other hand, such case folding can equate words that might better be kept apart. For instance, some proper nouns are distinguished by a capital letter at the beginning, like names (Black) or companies (General Motors).
* Stop-words. Words that are non-informative (articles, prepositions, pronouns, con- junctions, etc) are removed from the text. If we leave stop-words in the corpus when computing the model, we may end up with meaningless topics that are described with only stop-words, due to their high probability.

* Stemming. Stemming. It’s the process of reducing a word to its roots. Using stemming could reduce the vocabulary, however, words can lose its meaning in the process. For instance, the word international and interns could be reduced to intern. For this reason we must be careful about using a stemmer since they tend to be very ag- gressive. The Porter Steammer is one of the most used stemmers. It uses a set of rules applied in a succession of 5 steps. The first step gets rid of plurals and -ed or -ing suffixes, so we considerer that applying only that rule would be enough for our purpose.

* Hashtags. In Twitter they are used by users to mark keywords and categorize tweets. Unfortunately this is not always the case having some ambiguity in the way that users use them. For this reason we will only remove the hashtag and consider the sole word.

All these pre-processing tasks are planned to be done using the NLTK library for Python.

### Data Mining. 

We believe that using LDA in the URL content after the corresponding pre- processing should be quite straightforward. On the other hand, tweets are very short documents, limited to 140 characters. This may have an impact in the topic distribution. For this reason we propose the following:
* Group all the tweets per country.
* Represent them in a TF-IDF model.
* For each group, apply an Affinity Propagation clustering. An study [15] shows that, in comparison with K-means and SVD, Affinity Propagation performs best in clustering short text data with minimal cluster error.
* Combining all the tweets in a cluster we will have a new document. Having these documents we will then perform LDA.
We’ll be using the R packages LDA and APCluster in this data mining phase.

## Results

We present the results for 44627 documents in the URL content. Establishing 10 topics and selecting the top-10 words for each topic we obtain:
![](https://github.com/jrn1989/Twitter-Topic-Modeling/blob/master/graph1.png )

The text in the tweets is very short and it might have an impact in the topic distribution. So as we proposed in the milestone 2, we apply the affinity propagation algorithm to clusterize the tweets per country, where each cluster will correspond to a new document. It is important to tell that we had some difficulties with the implementation in R since it’s way too expensive in memory and time, specially for the countries of Indonesia and United States which are the two countries with the major number of tweets (in the order of 6000 and 20000 respectively). For this two we had to implement a different code in Java to calculate the similarity matrix. After this we were able to apply the clustering with the R script for Indonesia (it took approximately 2.5 hours) and we tried it for United States, unfortunately the computer crashed after 5 days and 36GB in memory. Eventually we decided to split the US tweets in 3 parts and apply the clustering. After that, we calculate a new similarity matrix for all the clusters in order to clusterize again. 
![](https://github.com/jrn1989/Twitter-Topic-Modeling/blob/master/graph2.png )

## Conclusions

Conclusions
Analysing the tweets and their shared URLs showed that the topics generated from the tweets and their URLs have only 56% of matching. Even though we only selected tweets with URLs, which means for each tweet there is a correspondent web page indicating the topics of both URLs and tweets, should theoretically be the same. The poor matching percentage is due to the fact that the tweet doesn’t normally describe what is in the shared URL. For instance, a user could shared a tweet like “check this out http://”. Another reason for such poor mismatching is the small size of the tweet; in the URL web page we can detect it easily, but for tweet (even though we combined them into clusters) there are still clusters that have only one or two tweets.
The result of the topics across countries should be interpreted carefully since not each coun- try have enough representation of tweets. Some countries have less than 10 tweets, so it is not a representative result. Also another aspect that should be take into account while considering the result of the topics, is how much is the percentage of English tweets to the total number of tweets for each country. Some countries have very low percentage of English tweets compared to the country total tweets like Brazil; in this case the result will present the portion of people writing tweets in English and not the whole population tweets. We can check that in the result, every topic is very representative by English speaking countries, and most of the top countries for each topic is an English speaking country. When the result is interpreted, the reader should know that only 2% of Twitter users set a location stamped to their tweets. So the result actually represent a subset of people/civilians who stamped the location in their twitter.

## References

[1] http://en.wikipedia.org/wiki/Topic_model

[2] Landauer, Thomas K., Peter W. Foltz, and Darrell Laham. ”An introduction to latent se-
mantic analysis.” Discourse processes 25.2-3 (1998): 259-284.

[3] http://en.wikipedia.org/wiki/Latent_semantic_analysis

[4] Hofmann, Thomas. ”Probabilistic latent semantic analysis.” Proceedings of the Fifteenth conference on Uncertainty in artificial intelligence. Morgan Kaufmann Publishers Inc., 1999.

[5] http://www.slideshare.net/NYCPredictiveAnalytics/ introduction-to-probabilistic-latent-semantic-analysis

[6] Blei, David M., Andrew Y. Ng, and Michael I. Jordan. ”Latent dirichlet allocation.” the Journal of machine Learning research 3 (2003): 993-1022.

[7] Blei, David M. ”Probabilistic topic models.” Communications of the ACM 55.4 (2012): 77-84.

[8] http://blog.echen.me/2011/08/22/introduction\ -to-latent-dirichlet-allocation/

[9] Lafferty, John D., and David M. Blei. ”Correlated topic models.” Advances in neural in- formation processing systems. 2005.

[10] D. Blei and J. Lafferty. Topic Models. In A. Srivastava and M. Sahami, editors, Text Mining: Classification, Clustering, and Applications . Chapman Hall/CRC Data Mining and Knowledge Discovery Series, 2009.

[11] Lee, Sangno, et al. ”An empirical comparison of four text mining methods.” System Sci- ences (HICSS), 2010 43rd Hawaii International Conference on. IEEE, 2010.

[12] Steyvers, Mark, and Tom Griffiths. ”Probabilistic topic models.” Handbook of latent se- mantic analysis 427.7 (2007): 424-440.

[13] Hong, Liangjie, and Brian D. Davison. ”Empirical study of topic modeling in twitter.” Proceedings of the First Workshop on Social Media Analytics. ACM, 2010.

[14] Zhao, Wayne Xin, et al. ”Comparing twitter and traditional media using topic models.” Advances in Information Retrieval. Springer Berlin Heidelberg, 2011. 338-349.

[15] Rangrej, Aniket, Sayali Kulkarni, and Ashish V. Tendulkar. ”Comparative study of clus- tering techniques for short text documents.” Proceedings of the 20th international confer- ence companion on World wide web. ACM, 2011.
