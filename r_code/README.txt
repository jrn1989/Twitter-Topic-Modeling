README

This readme file is to describe the purpose of different files for r code in this folder

cluster.r: this code is used to read from a folder containing a number of folder named after countries codes, each contains the tweets of that country
			the code calculate the tfidf of the tweets, calculate similarity matrix and apply affinity propagation clustering algorithm for each country
			for each country combine the similar tweets and save them into one file

lda.r: this code takes as an input a serie of text documents an apply the LDA algorithm to detect the topics
			