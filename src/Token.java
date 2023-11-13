import java.util.Comparator;
/**
 * Represents a token with a text string and a count of its occurrences.
 */
public class Token implements Comparable<Token> {

    /*
     * Shared across all token objects; belongs to the class, not the instance.
     */
    public static int countUniqueWords = 0;
    private int count = 1;
    private String str;

    /**
     * Constructs a new Token with the given string.
     * Also increments the count of unique words when called
     *
     * @param s The text string of the token.
     */
    public Token(String s) {
        ++countUniqueWords;
        this.str = s;
    }

    /**
     * Gets the count of occurrences for this token.
     *
     * @return The count of occurrences.
     */
    public int getCount() {
        return this.count;
    }

    /**
     * Increments the count of occurrences for this token.
     */
    public void incrementCount() {
        ++this.count;
    }

    @Override
    public boolean equals(Object obj) {
        /*
         * A non-empty, non-null, unequal String object should return false.
         * Default false
         */
        if (obj == null) {
            return false;
        } else // Content comparison := aString.equals(anotherString).
            if (this == obj) {
            /*
             * Reflexive x.equals(x) := true; ie, an object is equal to
             * itself.
             */
            return true;
        } else return this.str.equals(obj.toString());
    }

    /*
     * Tokens are not very special, so a standard String comparison is
     * sufficient. Call the String compareTo method.
     */
    @Override
    public int compareTo(Token t) {
        return this.str.compareTo(t.toString());
    }

    // This comparator will cause Collections.Sort to sort more frequent words first.
    /**
     * Because more frequent words should be sorted /before/ less
     * frequent words, the difference should be negative. A word
     * with a higher frequency will return a positive difference, so
     * to be sorted first the magnitude needs to be inverted.
     */
    public static Comparator<Token> CompFreqDesc = (Token tokenOne, Token tokenTwo) -> {
        int difference = tokenOne.getCount() - tokenTwo.getCount();
        return ((difference == 0) ? tokenOne.compareTo(tokenTwo) : -difference);
    }; // End of assigned lambda expression.

    public static Comparator<Token> CompLengthDesc = (Token tokenOne, Token tokenTwo) -> {
        int difference = tokenOne.str.length() - tokenTwo.str.length();
        return ((difference == 0) ? tokenOne.compareTo(tokenTwo) : -difference);
    };

    /**
     * Overridden toString() method to return the data of the token
     */
    @Override
    public String toString() {
        return this.str;
    }
}
