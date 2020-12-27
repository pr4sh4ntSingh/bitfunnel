Note- Please run following commands at root level of project. Sample data is provided in data folder

 create index 
--------------

java com.bitfunnel.preprocessing.doc_index <folderpath> <d> <snr> <m>

// m is optional. If not provided it will be computed by given formulae

Example1 (Absolute data path with m=1200)

java com.bitfunnel.preprocessing.doc_index "D:\FilePath\Scalable Algorithm\Assignment 1\version2\src\data" 0.2 .2 1200

Example2 (Relative data path  wiht default m)

java com.bitfunnel.preprocessing.doc_index "data" 0.2 .2 

Please Note that this method creates index file in binary format at the same folder. Which will be read by searching program.
-------------------------------------------------------------------------------------------------------


Searching query
---------------

java com.bitfunnel.search.doc_search <queryFile>

Example1

java com.bitfunnel.search.doc_search "query.txt"


Search Results are stored in "Result.txt" in working folder.