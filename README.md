# twitter-keyword-search
CLI tool for querying a list of tweets against keywords.

This tool accepts a text file, `tweets.txt`, containing a number of tweets as input (each line is one tweet), and provides a simple user interface fore querying the list of tweets against keywords. The system replies to keyword queries with the top 10 tweets that are most relevant to user query keyword(s).


## Section 1: Documentation
In addition to the description below, I have also commented my code to make it intuitively readable (start in KeywordSearcher.java).

The problem I am trying to solve is a similarity problem. How does one rank the "similarity" of a particular string to another? If you had infinite runtime and auxiliary space, you could probably make a very robust comparison, but I wanted to develop a solution that minimized auxiliary space and runtime complexity.

My implementation:

1) Reads in tweets from an external file and stores them in an immutable list since we know we will not be modifying these tweets.
2) After reading in the tweets, iterate through each word in each tweet, placing each word into its own an unordered HashSet, which is placed in a mutable ArrayList. In theory this is O(n) runtime but in practice it is much less as there are far fewer words than characters in a tweet.
3) After this preparation is done, take a search input from a user.
4) Break the search input into words, which are put into an unordered HashSet.
5) Compare the search HashSet to all other HashSets, producing a higher similarity score based on the percentage overlap of search words in the other set. The result of these comparisons are stored in another ArrayList. I use a custom data structure, SetSimilarityTuple, to store the similarity (we will sort the array by this) and the original tweet index (so we can return the original tweet when we are done comparing). This makes O(n) comparisons.
6) Sort the ArrayList of SetSimilarityTuples. This takes O(n log n).
7) Return the top 10 tweets with highest similarity scores.

I considered using an inverted index data structure for this project (Wikipedia article: https://en.wikipedia.org/wiki/Inverted_index) because this data structure is very good at doing full text searches. However I ultimately decided against it because of the space complexity -- an inverted index effectively would mean that I use an ArrayList of strings instead of a HashSet to hold my tweet text, then I would have to sort these ArrayLists to get my words in order, then I would thin out sentences by comparing each word to the word in my sorted query ArrayList. This doesn't seem worth it for me given that HashSet lookup time is very fast and if I am only rating based on common words, sets are more efficient.


## Section 2: Code
The code for my program should be attached alongside this file. It can be run by either doing:
* `./run.sh` Which runs a shell script that compiles and runs my Java code

Or:
* `javac KeywordSearcher.java` To compile the Java files
* `java KeywordSearcher` To run the Java files


## Section 3: Supporting files / dependencies
My code uses the Java standard library; as long as you have Java 8+ installed it should work.

With regard to unit tests, the similarity score of each tweet should be a perfect 1.0 if the user's input is identical to the tweet. To test this, try entering `Read both versions of a book written in a language and translated to another. See what is lost and what is added in translation and amaze.` at the query prompt using the default data, and the first tweet should be identical.
Also, when a search has completed, the results from the last search should not affect the next search (it should be possible to get a similarity score below the max similarity score from last search). To test this, simply do a search for `test` after the last tweet, and the similarity scores should all be 0 (there are no tweets with this keyword in my default dataset).


## Section 4: Work breakdown
All of the work was done by Warren Partridge (me).

Even if it's not super relevant, I want to briefly mention that I pivoted to doing this project after much trial and unsuccess with implementing Algorithm X with Dancing Links, an algorithm by Donald Knuth used to find solutions to the exact cover problem. My non-working code can be found at https://github.com/Warren-Partridge/algorithm-x
if you are curious. I wasn't able to figure out how to apply a set of sets of booleans as an input to DLX, or how to verify that a solution had been found when it seemed to be found.

