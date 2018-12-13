import java.util.*;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;


public class KeywordSearcher {
    final String TWEETS_FILEPATH = "tweets.txt";

    public List<String> tweets; // Immutable array of tweets read as input
    public ArrayList<HashSet<String>> tweetSets; // Sets of words in a particular tweet (basically inverted index but a set)
    public ArrayList<SetSimilarityTuple> similarities; // Mappings of tweet indices to similarity scores
    public int numEntriesToDisplay; // Number of entries to display per search (set to 10)


    public KeywordSearcher(int numEntries) {
        System.out.println("Welcome to Twitter Keyword Searcher!");
        try { // Try importing tweets from tweets.txt, newline delimited

            this.tweets = Files.readAllLines(Paths.get(TWEETS_FILEPATH));
            System.out.println("Tweets successfully read from " + TWEETS_FILEPATH + ".");
        } catch (IOException e){
            System.out.println("Failed to read tweets from " + TWEETS_FILEPATH + ". Printing error below:");
            System.out.println(e);
        }

        System.out.println("(To exit, type EXIT at the prompt.)");
        System.out.println("=========================================");

        this.tweetSets = new ArrayList<HashSet<String>>();
        this.similarities = new ArrayList<SetSimilarityTuple>();
        this.numEntriesToDisplay = numEntries;
    }


    public static void main(String[] args) {
        KeywordSearcher kws = new KeywordSearcher(10);
        kws.search();
    }


    private HashSet<String> makeSetFromSentence(String sentence) {
        // I considered using an ArrayList here, and then sorting the words to create a true inverted index.
        // However I decided to use HashSet instead because the time complexity is better for large datasets.
        // The main benefit to using an ArrayList would be to thin out tweets that don't share the same first few words,
        // but it seems to me that in order to be totally thorough, you would need to observe some significant % of the
        // ArrayList words. So using a HashSet seemed more efficient to me even if thinning tweets is not possible.
        //
        // With an ArrayList:
        //      Putting words into ArrayList: O(n)
        //      Sorting ArrayList: O(n log n)
        //      Checking whether a word is contained in sorted ArrayList: O(log n)
        //
        // With a HashSet:
        //      Putting words into HashSet: O(n)
        //      Checking whether a word exists in HashSet: O(1) (although unable to thin out known bad tweets)

        HashSet<String> result = new HashSet<String>();

        while (sentence.length() != 0) {
            int index = sentence.indexOf(" ");

            if (index < 0 && sentence.length() > 0) { // Case for last word in the sentence
                result.add(sentence.toLowerCase());

                return result;
            } else {
                result.add(sentence.substring(0, index).toLowerCase());
                sentence = sentence.substring(index + 1);
            }
        }

        return result;
    }


    private double getSimilarityBetweenSets(HashSet<String> set1, HashSet<String> set2) {
        if (set2.size() < set1.size()) return getSimilarityBetweenSets(set2, set1);

        int count = 0;
        for (String s : set1) {
            if (set2.contains(s)) count++;
        }

        int common = set1.size() + set2.size() - count;

        return (double) count / common;
    }


    public void search() {
        for (String tweet : this.tweets) { // Make sets of all input tweets to prepare for searching
            this.tweetSets.add(this.makeSetFromSentence(tweet));
        }

        Scanner reader = new Scanner(System.in);
        boolean searching = true;

        while (searching) {
            System.out.print("Please enter a tweet search query: ");
            String query = reader.nextLine();

            if (query.compareTo("EXIT") == 0) {
                System.out.println("Goodbye!");
                break;
            }
            query.toLowerCase();

            HashSet<String> querySet = this.makeSetFromSentence(query);

            for (int i = 0; i < this.tweetSets.size(); i++) {
                SetSimilarityTuple similarity = new SetSimilarityTuple(i, this.getSimilarityBetweenSets(querySet, this.tweetSets.get(i)));
                this.similarities.add(similarity);
            }

            Collections.sort(this.similarities);

            // Don't display more entries than is possible
            int numEntriesPossibleToDisplay = this.numEntriesToDisplay < this.tweetSets.size() ? this.numEntriesToDisplay : this.tweetSets.size();

            System.out.format("Here are the top %d tweets I found most similar to your query:\n", this.numEntriesToDisplay);
            System.out.format("%7s | %16s | %s", "Tweet #", "Similarity Score", "Tweet Content\n");
            System.out.println("-----------------------------------------------------");
            for (int i = 0; i < numEntriesPossibleToDisplay; i++) {
                System.out.format("%7d | %16f | %s\n", i, this.similarities.get(i).similarityScore, this.tweets.get(this.similarities.get(i).index));
            }
            System.out.println();

            this.similarities = new ArrayList<SetSimilarityTuple>();
        }

        reader.close();
    }
}
