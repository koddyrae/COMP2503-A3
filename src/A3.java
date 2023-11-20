
import java.util.Scanner;
import java.util.Iterator;


public class A3 
{
   /* The lists (trees) of words. Alphabetic, by Frequency 
      and by length. */
   private static final BST<Token> wordsByNaturalOrder = new BST<>();
   private static final BST<Token> wordsByFreqDesc = new BST<>(Token.CompFreqDesc);
   private static final BST<Token> wordsByLength = new BST<>(Token.CompLengthDesc);
   
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

   private static int totalwordcount = 0;
   private static int stopwordcount = 0;

   private static final Scanner inp = new Scanner(System.in);

   public static void main(String[] args)
   {	
       readFile();
       removeStop();
       createFreqLists();
       printResults();
   }

   private static void printResults()
   {
       System.out.println("Total Words: " + totalwordcount);
       System.out.println("Unique Words: " + wordsByNaturalOrder.size()); 
       System.out.println("Stop Words: " + stopwordcount);
       System.out.println();
       System.out.println("10 Most Frequent"); 

       Iterator<Token> wordsByFreqDescInOrder = new BST.InOrderIterator<>(wordsByFreqDesc);
       int i = 10;
       while ((i > 0) && wordsByFreqDescInOrder.hasNext()) {
           --i; // Decrement the sentinel.
           Token t = wordsByFreqDescInOrder.next();
           // DONE: Whenever you print a Token, print the word, its length, and the number of times it occurred, separated by colons.
           System.out.println(t + ":" + t.toString().length() + ":" + t.getCount());
       }

       System.out.println();
       System.out.println("10 Longest");

       /* TODO:
        * Use an iterator to traverse the wordsByLength in-order, and print the first 10
        */

       System.out.println();
       System.out.println("The longest word is " + wordsByLength.minimum());
       System.out.println("The average word length is " + avgLength());
       System.out.println();        
       System.out.println("All");

       /* TODO:
        * Use an iterator to traverse the wordsByNaturalOrder in-order, and print all elements in the tree
        */

       System.out.println();
       
       System.out.println("Alphabetic Tree: (Optimum Height: " + 
             optHeight(wordsByNaturalOrder.size()) + ") (Actual Height: " 
             + wordsByNaturalOrder.height() + ")");
       System.out.println("Frequency Tree: (Optimum Height: " + 
             optHeight(wordsByFreqDesc.size()) + ") (Actual Height: " 
             + wordsByFreqDesc.height() + ")");
       System.out.println("Length Tree: (Optimum Height: " + 
             optHeight(wordsByLength.size()) + ") (Actual Height: " 
             + wordsByLength.height() + ")");
   }
   
   /* Read the file and add words to the list/tree. 
    */
   private static void readFile() {
       while (inp.hasNext()) {
           /*
            Get the next word, convert to lower case, strip out blanks and non-alphabetic
            characters.
           */
           String word = inp.next().toLowerCase().trim().replaceAll("[^a-z]", "");

           if (word.length() > 0) {
               /*
                Create a new token object, if not already in the wordsByNaturalOrder,
                add the token to the BST, otherwise, increase the frequency count of the
                object already in the tree.
               */
               Token maybeNewToken = new Token(word),
                       existingToken = wordsByNaturalOrder.find(maybeNewToken);
               if (existingToken == null) {
                   wordsByNaturalOrder.add(maybeNewToken);
               } else {
                   existingToken.incrementCount();
               }
           }
       }
   }

   /* Create the frequency and length lists. */
   private static void createFreqLists()
   {
	   // TODO:
	   // Use your implementation of the iterator interface
	   // for the BST class.
	   
	   // Make sure you only add words that have occurred more than twice
	   // to the tree ordered by word frequency.
	   
	   // All words in the original tree must be added to tree ordered by word length
   }

   /* Calculate the average length of words stored the wordsByNaturalOrder tree*/
   private static int avgLength()
   {
	   // TODO:
	   return 0;
   }

   /* Remove stop words from the tree. */
   private static void removeStop()
   {
	   // TODO:
   }

   /* Calculate the optimal height for a tree of size n. 
      Round to an int. 
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
