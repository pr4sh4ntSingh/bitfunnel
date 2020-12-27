# Bitfunnel


This project is assignment work for Scalable Algorithms at IIT Hyderabad M.Tech  (Summer 2019-20).
In this assignment, I implemented Bloom filter based document indexing and retrieval. BitFunnel indexing is used by Bing search engine.
The input to the program is a folder consisting of a set of English text files. 



Program has two parts:

## Indexing:

The program first creates index to the folder's contents to create a single index file and store it in the specified storage location.


## Searching:

The second part of the program is search, which given a list of search terms, will output the names of the files that contains all the search terms.



# Detailed Explanation and how to use code:

## Indexing

To create index use following class:

```
java com.bitfunnel.preprocessing.doc_index <folderpath> <d> <snr> <m>
```

Here is explanation of variables used:


* $d$  : is the expected bit density or the expected fraction of bits which are turned
ON in any documents bloom filter.

* $snr$ ($\phi$) : real number ’snr’ which is the minimum signal to noise ratio to be achieved.

* $m$ : number of bits in bloof filter.  If m is not given program will compute optimal m as per proof discussed in lecture. $m$ is given as: ($a_i$ and $k_i$ are defined below).
 
$
m= \dfrac{1}{ln\dfrac{1}{1-d}} \sum_{i=1}^{N} (a_i * k_i)
$
  


* Example :
    ```
    java com.bitfunnel.preprocessing.doc_index "D:\FilePath\Scalable Algorithm\Assignment 1\version2\src\data" 0.2 .2 1200
    ```
    OR with optimal value of m (automatically computed)
    ```
    java com.bitfunnel.preprocessing.doc_index "D:\FilePath\Scalable Algorithm\Assignment 1\version2\src\data" 0.2 .2
    ```


###  Intermediate files Generated thorough indexing:

* **index** : index is stored in this file and used to search query index. Once index is created we don't need to look up original text files.
* **frequency.txt** : This file contains frequency of every word. Binary equivalent of this file is used to get $a_i$ for a word $w_i$.
* **hashfunctions.txt** : This file contains number of hashfunctions and their seed values for each token. This helps to achive given signal to noise ratio and expected bit density $\phi$. Number of hashfunctions for a given word $w_i$ is calculated as below: ($a_i$ is frequency of word $i$)

$
k_i = min  (3, log_d(\dfrac{a_i}{ 1-a_i} *\phi))
$

* **invertedIndex** : This is bitFunnel index. This is Transpose of index created earlier. Funnelling through this inverted index where query bits are ON, speeds up query process and we don't need to go through all signatures.

## Searching

For a given word we first create bloom filter using precomputed $a_i$, $k_i$ and seeds that are stored in hashfunctions.txt file. Once we get bloomfilter for query word we apply bitfunnel algo through inverted index and find out files which contains the query word. Please be noted that, this is probabilistic data structure, and this data structure will give some of false positives (whose upper bound can be derived) but zero false negative.


```
java com.bitfunnel.search.doc_search <queryFile>
```
Program reads every word from queryFile and search it in index and writes files matched files names in Resutl.txt in current working directory.

***

### More information about proofs and bitfunnel literatures can be found at  paepe by Broder et al.  ”Network Applications of Bloom Filters:  A Survey”,Internet Mathematics 1 (2005)
