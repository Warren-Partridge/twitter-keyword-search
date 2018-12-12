import java.util.*;
import java.util.Scanner;


public class KeywordSearcher {

    public ArrayList<String> tweets;
    public ArrayList<HashSet<String>> tweetSets;
    public ArrayList<SetTuple> similarities;
    public int numEntriesToDisplay;
//    public Map<int, double> similiarities;

    public KeywordSearcher(int numEntries) {
        this.tweets = new ArrayList<String>();
        this.tweets.add("Google unveils its Container Engine to run apps in the best possible way on its cloud flip.it/RPQLZ");
        this.tweets.add("Yahoo tops list of 10 most active tech acquirers in 2013 flip.it/g5c2f");
        this.tweets.add("Tresorit opens its end-to-end encrypted file-sharing service to the public flip.it/EK1IZ");

        this.tweetSets = new ArrayList<HashSet<String>>();
        this.similarities = new ArrayList<SetTuple>();
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
//            System.out.println("Breaking down sentence " + sentence);

            int index = sentence.indexOf(" ");

//            if (index < 0 && sentence.length() == 0) {
//                System.out.println("Weirdness");
//                return result;
//            } else
//
            if (index < 0 && sentence.length() > 0) { // Case for last word in the sentence
                // TODO: Make all lowercase
                result.add(sentence);

                return result;
            } else {
                result.add(sentence.substring(0, index));
                // TODO: Make all lowercase
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

        int union = set1.size() + set2.size() - count;

        return (double) count / union;
    }

    public void search() {
        for (String tweet : this.tweets) { // Make sets of all input tweets to prepare for searching
            this.tweetSets.add(this.makeSetFromSentence(tweet));
        }

        Scanner reader = new Scanner(System.in);
        String query = reader.nextLine();

        HashSet<String> querySet = this.makeSetFromSentence(query);

        for (int i = 0; i < this.tweetSets.size(); i++) {
            SetTuple similarity = new SetTuple(i, this.getSimilarityBetweenSets(querySet, this.tweetSets.get(i)));
            this.similarities.add(similarity);
        }

        Collections.sort(this.similarities);

        // Don't display more entries than is possible
        int numEntriesPossibleToDisplay = this.numEntriesToDisplay < this.tweetSets.size() ? this.numEntriesToDisplay : this.tweetSets.size();

        System.out.format("%7s | %16s | %s", "Tweet #", "Similarity Score", "Tweet Content\n");
        System.out.println("------------------------------------------");
        for (int i = 0; i < numEntriesPossibleToDisplay; i++) {
            System.out.format("%7d | %16f | %s\n", i, this.similarities.get(i).similarityScore, this.tweets.get(this.similarities.get(i).index));
        }



        System.out.println(querySet);
        reader.close();
    }
}