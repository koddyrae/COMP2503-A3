import java.util.Comparator;
/**
 * Represents a token with a text string and a count of its occurrences.
 */
public class Token implements Comparable<Token> {

    private int count = 1;
    private final String str;

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

    /**
     * Override equals method to compare two objects on if they are equal
     * @param obj object to be compared to calling object
     * @return b boolean true if equal, false otherwise
     */
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

    /**
     *  Overridden String compareTo method since Tokens are not very special,
     *  so a standard String comparison is sufficient.
     */
    @Override
    public int compareTo(Token t) {
        return this.str.compareTo(t.toString());
    }

    // This comparator will cause Collections.Sort to sort more frequent words first.
    /**
     * Comparator to compare the frequency of two tokens.
     */
    public static Comparator<Token> CompFreqDesc = (Token tokenOne, Token tokenTwo) -> {
        /*
        * Because more frequent words should be sorted /before/ less
        * frequent words, the difference should be negative. A word
        * with a higher frequency will return a positive difference, so
        * to be sorted first the magnitude needs to be inverted.
         */
        int difference = tokenOne.getCount() - tokenTwo.getCount();
        return ((difference == 0) ? tokenOne.compareTo(tokenTwo) : -difference);
    }; // End of assigned lambda expression.

    /**
     * Comparator to sort longer words before shorter words, and when two words have the same
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

    /**
     * Used to format the object for pretty-printing with the TreePrinter class.
     * @return A string formatted with the string contents of the token, its length, and
     * its frequency, separated by colons and no whitespace.
     */
    public String format() {
        /* DONE: Whenever you print a Token, print the word, its length, and the number of
         * times it occurred, separated by colons.
         */
        return str + ":" + str.length() + ":" + count;
    }
}
