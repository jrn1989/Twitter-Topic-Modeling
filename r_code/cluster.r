library('tm')
library('apcluster')
dir = list.files("F:\\Workspace\\twitter_crawl\\tweets")
apres=vector()
for ( i in 1:length(dir) ){
  print(dir[i])
  conDir = list.files(paste("F:\\Workspace\\twitter_crawl\\tweets\\",dir[i],sep=""))
  if(!grepl(".txt",dir[i])){
    if(length(conDir) > 1) {
      commonDoc <- vector()
      for ( j in 1:length(conDir)){
        commonDoc[j]=readChar(paste("F:\\Workspace\\twitter_crawl\\tweets\\",dir[i],"\\",conDir[j],sep=""),140)
        names(commonDoc)[j] <- conDir[j]
        if(j==1)
          cor = termFreq(PlainTextDocument(commonDoc[j],id =conDir[j], heading = conDir[j], description=conDir[j], origin = conDir[j], localmetadata= conDir[j], author="ahmed"))
        else{
          cor = c(cor, termFreq(PlainTextDocument(commonDoc[j],id =conDir[j], heading = conDir[j], description=conDir[j], origin = conDir[j], localmetadata= conDir[j], author="ahmed")))
          cor$dimnames[2]$Docs[j]=j
        }
      }
      cor$dimnames[2]$Docs[1]=1
      tfidf=weightTfIdf(cor,normalize = TRUE)
      sim <- inspect(tfidf)
      commonDocSim <- negDistMat(t(sim), r=2)
      cluster = apcluster(commonDocSim, details=TRUE)
      for(ind in 1:length(cluster)){
        st=''
        for(dc in 1:length(cluster[[ind]]))
          st=paste(st,commonDoc[as.integer(names(cluster[[ind]])[dc])],sep=" ")
        
        write(st, file = paste('F:\\out\\',dir[i],'Cluster',ind,'.txt',sep=''))
      }
    }
    else{
      st=readChar(paste("F:\\Workspace\\twitter_crawl\\tweets\\",dir[i],"\\",conDir[1],sep=""),140)
      write(st, file = paste('F:\\out\\',dir[i],'.txt',sep=''))
    }
  }
}
