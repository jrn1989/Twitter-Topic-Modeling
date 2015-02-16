# DMKM Bucharest 2014
# Jose Robles, Ahmed Abdelwahab

# Required libraries, dont forget to check if you already have them installed
library("lda")
library("reshape")
library("ggplot2")
library("scales")


# Dont forget to set up you wroking directory to the source file location
setwd("C:/Users/jrn/Desktop/NLP/DemoNLP_Milestone3")

# Select the appropiate python script either if you are dealing with 
# tweets or html files
system('C:\\Python27\\python.exe preprocessing_html.py')
#system('C:\\Python27\\python.exe preprocessing_tweet.py')

# We get the list of files that have been preprocessed
filenames = list.files(pattern="\\.txt") 
filenames
gc() 
docs = c()
# We retrieve the text of those files
for(filename in filenames){
  docs = c(docs,paste(readLines(file(filename)),collapse="\n")) 
  #gc()
}

# We put all the text together
obj_list <- lapply(docs,paste,collapse=" ") 
obj_vec <- as.vector(obj_list) 

# We get the tokens
documents2 <- lexicalize(obj_vec, lower=TRUE)

# We specify the number of topics we want
K=10

# We apply LDA
result2 <- lda.collapsed.gibbs.sampler(documents2$documents,K,  documents2$vocab, 30, 0.1, 0.1, compute.log.likelihood=TRUE) 

# To see the top documents for each topic
#top.topic.documents(result2$document_sums, num.documents = 20, alpha = 0.1)

# to see the top words for each topic
top.topic.words(result2$topics, 10, by.score=TRUE)

# Dont forget to put a label to the topics that you indentify
tt = c("Business","Market","Sports","Social","Career","Places","Activities","Cooking","Twitter","Fun")

# We do some operation to have an aggregated table (group by the country id)
# for the topics and countries
d = prop.table(result2$document_sums, margin=2)*100
colnames(d) = substr(filenames,1,2)
d <- d[,colSums(is.na(d))<nrow(d)]
d=as.data.frame(t(aggregate(t(d),list(colnames(d)), mean)))

# If you want to store a summary of all the data
# write.table(d, file = "Results.csv", sep=",",row.names=FALSE)

# More operations have an stacked table for the stacked plot
dat = d[-1,]
row.names(dat) <- tt
q = as.character.numeric_version(d[1,(1:length(d))])
colnames(dat) = q

datm=melt(cbind(dat,ind = rownames(dat)),measure.vars = q, is.vars=c('ind'))
class(datm$value)
datm$value = as.numeric(as.character(datm$value))

ggplot(datm,aes(x = variable,y = value,fill = ind)) + 
  geom_bar(position = "fill") + xlab("Countries") + ylab("Topics") +
  scale_y_continuous(labels = percent_format())






