# DMKM Bucharest 2014
# Jose Robles, Ahmed Abdelwahab
# Preprocessing of tweets (.txt files)

# Make sure to have the following libraries, especially the full nltk library

import os
import nltk, re
from urllib import urlopen
from string import lower
from nltk.corpus import wordnet
from bs4 import BeautifulSoup
import lxml
from lxml.html.clean import Cleaner

# PORTER STEMMER
# ONLY STEP 1 IMPLEMENTATION

#used in all steps
_c =    "[^aeiou]"          # consonant
_v =    "[aeiouy]"          # vowel
_C =    _c + "[^aeiouy]*"    # consonant sequence
_V =    _v + "[aeiou]*"      # vowel sequence

Mgre0 = re.compile("^(" + _C + ")?" + _V + _C)               # [C]VC... is m>0
Meq1 = re.compile("^(" + _C + ")?" + _V + _C +"(" + _V + ")"+ "?" + "$")  # [C]VC[V] is m=1
Mgre1 = re.compile("^(" + _C + ")?" + _V + _C + _V + _C)        # [C]VCVC... is m>1
CVCending = re.compile(_C + _v + "[^aeiouwxy]$")
vstem   = re.compile("^(" + _C + ")?" + _v)                   # vowel in stem
DoubleConsonant=  re.compile(r"([^aeiouylsz])\1$")   #matches double consonants excpet l s and z
removeEndingPunc  =  re.compile(r"[^a-z]+$")

def clean_html(html, remove_entities=False):
    p = re.compile("<(script|style).*?>.*?</(script|style)>", re.DOTALL)
    cleaned = re.sub(p, "", html)               # remove inline js/css
    cleaned = re.sub("<(.|\n)*?>", "", cleaned) # remove remaining html tags
    if remove_entities: cleaned = re.sub("&[^;]*; ?", "", cleaned)
    return cleaned

def stem(parms):
    stems = []
    for word in parms:

        ######## step 0               pre-process words
        word = lower(word)
        word = re.sub(removeEndingPunc,"",word)

        if len(word) < 3:            #don't stem if word smaller than 3
            stems.append(word)
            continue
        if word[0] == 'y': word = 'Y' + word[1:]      #make sure initial Y is not considered a vowel

       ##Step 1a
        ## SSES -> SS                         caresses  ->  caress
        ## IES  -> I                          ponies    ->  poni
        ##                                    ties      ->  ti
        ## SS   -> SS                         caress    ->  caress
        ## S    ->                            cats      ->  cat
        ## this could be better even if the match string is not in the target string
        ## sub still returns a string, so i have to check twice, is there a better way to do this

        if word[-1] == 's' and word[-2] != 's':
            if word[-4:] == 'sses':
                word = word[:-4] + 'ss'
            elif word[-3:] == 'ies':
                word = word[:-3] +  'i'
            else:
                word = word[:-1]

       ## Step 1b

        ##     (m>0) EED -> E                     feed      ->  feed ??? huh m is < 0
        ##                                        agreed    ->  agree
        ##     (*v*) ED  ->                       plastered ->  plaster
        ##                                        bled      ->  bled
        ##     (*v*) ING ->                       motoring  ->  motor
        ##                                        sing      ->  sing


        ## The rule to map to a single letter causes the removal of one of the double
        ## letter pair. The -E is put back on -AT, -BL and -IZ, so that the suffixes
        ## -ATE, -BLE and -IZE can be recognised later. This E may be removed in step
        ## 4.

        flag = None                         #only set to 1 2nd and 3rd steps are taken
        if word[-3:] == 'eed':                    # m>0   eed -> ee
            if Mgre0.search(word[:-3]):
                word = word[:-3] + "ee"

        elif word[-2:] == 'ed':                   # *v* ed
            if vstem.search(word[:-2]):
                word = word[:-2]
                flag = 1
        elif word[-3:] == 'ing':                  # *v* ing
            if vstem.search(word[:-3]):
                word = word[:-3]
                flag = 1

        # step 1b1
        ## If the second or third of the rules in Step 1b is successful, the following
        ## is done:

        ##     AT -> ATE                       conflat(ed)  ->  conflate
        ##     BL -> BLE                       troubl(ed)   ->  trouble
        ##     IZ -> IZE                       siz(ed)      ->  size
        ##     (*d and not (*L or *S or *Z))
        ##        -> single letter
        ##                                     hopp(ing)    ->  hop
        ##                                     tann(ed)     ->  tan
        ##                                     fall(ing)    ->  fall
        ##                                     hiss(ing)    ->  hiss
        ##                                     fizz(ed)     ->  fizz
        ##     (m=1 and *o) -> E               fail(ing)    ->  fail
        ##                                     fil(ing)     ->  file


        if flag:                                                # go on to part 1b2
            if word[-2:] == 'at':                               # at -> ate
                word = word[:-2] + 'ate'
            elif word[-2:] == 'bl':                             # bl -> ble
                word = word[:-2] + 'ble'
            elif word[-2:] == 'iz':                             # iz -> ize
                word = word[:-2] + 'ize'
            elif DoubleConsonant.search(word):                  # the word (so far) ends in a double consonant except
                word = word[:-1]                                # for l , s and z, replace with single consonant
            elif CVCending.search(word) and Meq1.search(word):  # m= 1 and word ends in CVC sequence
                word = word + 'e'                               # add an e


        # Not required step

        #step 1c
        # (*v*) Y -> I                    happy        ->  happi
        #                                 sky          ->  sky

        #if word[-1:] == 'y':                    ##change to [-1]
         #   if vstem.search(word[:-1]):
          #      word = word[:-1] + 'i'

        if word != '':
        #if not wordnet.synsets(word):
            stems.append(word)

    return stems

# Main function
# It will take as input all the txt files that finds in the current directory
# and subdirectories

for root, dirs, files in os.walk("."):
    for file in files:
        if file.endswith(".html"):

            # We get the html file
            #url = str(i)+".html"
            url = os.path.join(root, file)
            html = urlopen(url).read()

            # We remove all the tags from the html file
            raw = nltk.clean_html(html)

            # We tokenize (list of words)
            cleanText = nltk.word_tokenize(raw)

            # We filter the words, only for those that have more than 4 characters,
            # they are not a stopword and belong to the english vocabulary

            cleanText = [w.lower() for w in cleanText]
            bad = ["null","true","false","width","http","height","length","type","u.","false",'option','acest']

            filtered_words = [w for w in cleanText if len(w) >=4 and w not in nltk.corpus.stopwords.words('english') and wordnet.synsets(w)
            and w not in bad]

            # From all the remaining words, we apply stemming
            final = stem(filtered_words)

            # Just to print the final number of words
            # print(len(final))

            # We store each processed text file in a differetn in the curretn directory
            with open(file[:-4]+"txt", 'w') as f:
                for s in final:
                    f.write(s + ' ')
                f.write('\n')
