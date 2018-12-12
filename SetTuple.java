import java.util.*;

public class SetTuple implements Comparable<SetTuple> {
    int index; // Position in the KeywordSearcher.tweets ArrayList
    double similarityScore;

    public SetTuple(int i, double simScore) {
        this.index = i;
        this.similarityScore = simScore;
    }

    @Override
    public int compareTo(SetTuple t) {
        if (this.similarityScore < t.similarityScore) {
            return 1;
        } else if (this.similarityScore == t.similarityScore) {
            return 0;
        } else {
            return -1;
        }
    }
}