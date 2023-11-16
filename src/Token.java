import java.util.Comparator;
/**
 * Represents a token with a text string and a count of its occurrences.
 */
public class Token implements Comparable<Token> {

    private int count = 1;
    private String str;
    public boolean stopWord;

    /**
     * Constructs a new Token with the given string.
     * Also increments the count of unique words when called
     *
     * @param s The text string of the token.
     */
    public Token(String s) {
        this.str = s;
    }

    /**
     * An alternative constructor to create a stop word, which aides in removal of the stop word later in the program.
     * @param s The text of the string token.
     * @param b Whether or not the token is a stop word.
     */
    public Token(String s, boolean b) {
        this.str = s;
        this.stopWord = b;
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

    /**
     * Sort longer words before shorter words, and when two words have the same
     * length they are sorted alphabetically.
     */
    public static Comparator<Token> CompLengthDesc = (Token tokenOne, Token tokenTwo) -> {
        int difference = tokenOne.toString().length() - tokenTwo.toString().length();
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
