
import java.util.Scanner;
import java.util.Iterator;

/**
 * Main class to handle main functionality of the program
 */
public class A3 
{
   /* The lists (trees) of words. Alphabetic, by Frequency 
      and by length. */
   private static final BST<Token> wordsByNaturalOrder = new BST<>();
   private static final BST<Token> wordsByFrequencyDescending = new BST<>(Token.CompFreqDesc);
   private static final BST<Token> wordsByLengthDescending = new BST<>(Token.CompLengthDesc);
   
   // there are 103 stopwords in this list
   private static final String[] stopwords = {
         "a","about","all","am","an","and","any","are","as","at", 
         "be","been","but","by","can","cannot","could", "did", "do", "does", 
         "else", "for", "from", "get", "got", "had", "has", "have", "he", "her", 
         "hers", "him", "his", "how", "i", "if", "in", "into", "is", "it", 
         "its",  "like", "more", "me", "my", "no", "now", "not", "of", "on", 
         "one", "or", "our", "out", "said", "say", "says", "she", "so", "some",
         "than", "that", "thats", "the", "their", "them", "then", "there", "these", "they", "this", 
         "to", "too", "us", "upon", "was", "we", "were", "what", "with", "when", 
         "where", "which", "while", "who", "whom", "why", "will", "you", "your", "up", "down", "left", "right",
         "man", "woman", "would", "should", "dont", "after", "before", "im", "men"
   };

   private static int totalWordCount = 0;
   private static int stopWordCount = 0;

   private static final Scanner standardInput = new Scanner(System.in);

    /**
     * Main Method
     * @param args command lines args
     */
   public static void main(String[] args)
   {	
       readFile();
       removeStop();
       createFreqLists();
       printResults();
   }

    /**
     * Method to print results of the required trees in desired orders
     */
   private static void printResults()
   {
       System.out.println("Total Words: " + totalWordCount);
       System.out.println("Unique Words: " + wordsByNaturalOrder.size()); 
       System.out.println("Stop Words: " + stopWordCount);

       System.out.println("\n10 Most Frequent");
       //To print words in order of descending frequency
       Iterator<Token> wordsByFreqDescInOrder = new BST.InOrderIterator<>(wordsByFrequencyDescending);
       int i = 10;
       while ((i > 0) && wordsByFreqDescInOrder.hasNext()) {
           --i; // Decrement the sentinel.
           Token t = wordsByFreqDescInOrder.next();
           // DONE: Whenever you print a Token, print the word, its length, and the number of times it occurred, separated by colons.
           System.out.println(t + ":" + t.toString().length() + ":" + t.getCount());
       }

       System.out.println("\n10 Longest");
       //To print words in order of descending length
       Iterator<Token> wordsByLengthInOrder = new BST.InOrderIterator<>(wordsByLengthDescending);
       i = 10; //Reset the variable
       while ((i > 0) && wordsByLengthInOrder.hasNext()) {
           --i;
           Token t = wordsByLengthInOrder.next();
           System.out.println(t + ":" + t.toString().length() + ":" + t.getCount());
       }
       if (wordsByLengthDescending.size() == 0) {
           System.out.println("\nThe longest word is: NONE [exceptional case].");
           System.out.println("The average word length is: NONE [exceptional case].");
       } else {
           System.out.println("\nThe longest word is " + wordsByLengthDescending.minimum());
           System.out.println("The average word length is " + avgLength());
       }

       System.out.println("\nAll");
       //To print words in natural order
       Iterator<Token> wordsByNaturalOrderInOrder = new BST.InOrderIterator<>(wordsByNaturalOrder);
       while (wordsByNaturalOrderInOrder.hasNext()) {
           Token t = wordsByNaturalOrderInOrder.next();
           System.out.println(t + ":" + t.toString().length() + ":" + t.getCount());
       }

       //To print each trees height
       System.out.println();
       System.out.println("Alphabetic Tree: (Optimum Height: " + 
             optHeight(wordsByNaturalOrder.size()) + ") (Actual Height: " 
             + wordsByNaturalOrder.height() + ")");
       System.out.println("Frequency Tree: (Optimum Height: " + 
             optHeight(wordsByFrequencyDescending.size()) + ") (Actual Height: "
             + wordsByFrequencyDescending.height() + ")");
       System.out.println("Length Tree: (Optimum Height: " + 
             optHeight(wordsByLengthDescending.size()) + ") (Actual Height: "
             + wordsByLengthDescending.height() + ")");
   }
   
   /**
    * Method to read the file and add words to the list/tree.
    */
   private static void readFile() {
       while (standardInput.hasNext()) {
           /*
            Get the next word, convert to lower case, strip out blanks and non-alphabetic
            characters.
           */
           String word = standardInput
                   .next()
                   .toLowerCase()
                   .trim()
                   .replaceAll("[^a-z]", "")
                   ;

           if (word.length() > 0) {
               totalWordCount++;
               /*
                Create a new token object, if not already in the wordsByNaturalOrder,
                add the token to the BST, otherwise, increase the frequency count of the
                object already in the tree.
               */
               Token maybeNewElement = new Token(word),
                       existingElement = wordsByNaturalOrder.find(maybeNewElement);
               if (existingElement == null) {
                   wordsByNaturalOrder.add(maybeNewElement);
               } else {
                   existingElement.incrementCount();
               }
           }
       }
   }

   /**
    * Method to create the frequency and length lists.
    */
   private static void createFreqLists() {
       // Use your implementation of the iterator interface
       // for the BST class.

       // Make sure you only add words that have occurred more than twice
       // to the tree ordered by word frequency.

       // All words in the original tree must be added to tree ordered by word length

       Iterator<Token> iterator = new BST.InOrderIterator<>(wordsByNaturalOrder);

       // Iterate over the words in the tree
       while (iterator.hasNext()) {
           // Get the next token (word) from the iterator
           Token token = iterator.next();

           if (token.getCount() > 2) {
               // If yes, add the word to the wordsByFreqDesc tree (ordered by frequency)
               wordsByFrequencyDescending.add(token);
           }

           // Add the word to the wordsByLength tree (ordered by length)
           wordsByLengthDescending.add(token);
       }
   }

   /**
    * Method to calculate the average length of words stored the wordsByNaturalOrder tree
    */
    private static int avgLength()
    {
        int totalLength = 0;
        int wordCount = 0;
        Iterator<Token> iterator = new BST.InOrderIterator<>(wordsByNaturalOrder);

        // Iterate over the words in the tree
        while (iterator.hasNext()) {
            Token token = iterator.next();
            totalLength += token.toString().length();
            wordCount += 1;
        }

        // Calculate and return the average length
        // If there are words in the tree, compute the average; otherwise, return 0 to avoid division by zero
        return (wordCount > 0) ? totalLength / wordCount : 0;
    }


    /**
     * Method to check and remove the array of stop words from the BST if present.
     */
   private static void removeStop()
   {
       BST.LevelOrderIterator<Token> iterator = new BST.LevelOrderIterator<>(wordsByNaturalOrder);
       while (iterator.hasNext()) {
           Token currentElement = iterator.next();
           for (String word : stopwords) {
               Token stopWord = new Token(word);
               if (currentElement.equals(stopWord)) {
                   wordsByNaturalOrder.delete(currentElement);
                   stopWordCount++;
               }
           }
       }
   }

   /** Method to calculate the optimal height for a tree of size n.
    *  Rounded to an int.
    */
   private static int optHeight(int n)
   {
	     double h = Math.log(n+1) / Math.log(2) - 1;  
	     if (Math.round(h) < h)
	    	  return (int)Math.round(h) + 1;
	     else
	    	  return (int)Math.round(h);
   }
}
