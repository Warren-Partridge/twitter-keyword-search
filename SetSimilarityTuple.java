import java.util.*;

public class SetSimilarityTuple implements Comparable<SetSimilarityTuple> {
    // I use this class to store (index : similarity) key-value pairs
    // in an ArrayList so that they may be sorted by similarity later.
    // I implement comparable because I don't want to search by the
    // index (obviously) and if I just used a Map with the similarity as
    // the key, my program would break when faced with two identical
    // similarity scores.

    int index; // Position in the KeywordSearcher.tweets ArrayList
    double similarityScore;

    public SetSimilarityTuple(int i, double simScore) {
        this.index = i;
        this.similarityScore = simScore;
    }

    @Override
    public int compareTo(SetSimilarityTuple t) {
        if (this.similarityScore < t.similarityScore) {
            return 1;
        } else if (this.similarityScore == t.similarityScore) {
            return 0;
        } else {
            return -1;
        }
    }
}
