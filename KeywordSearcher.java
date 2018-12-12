import java.util.*;
import java.util.Scanner;


public class KeywordSearcher {

    public ArrayList<String> tweets;

    public KeywordSearcher() {
        this.tweets = new ArrayList<String>();
        this.tweets.add("Google unveils its Container Engine to run apps in the best possible way on its cloud flip.it/RPQLZ");
        this.tweets.add("Yahoo tops list of 10 most active tech acquirers in 2013 flip.it/g5c2f");
        this.tweets.add("Tresorit opens its end-to-end encrypted file-sharing service to the public flip.it/EK1IZ");
    }

    public static void main(String[] args) {
        KeywordSearcher kws = new KeywordSearcher();
        kws.search();
    }

    public void search() {
        Scanner reader = new Scanner(System.in);

        System.out.println(tweets);
        int n = reader.nextInt();
        System.out.println(n);
        reader.close();
    }
}