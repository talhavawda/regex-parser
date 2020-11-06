/*
	This class was given.
	We are allowed to use our own design for this class (i.e. can modify or restructure as we wish)

	What we changed:
		1. Created a DfaState class to represent a set of NFA States (See DfaState.java)
			-> Thus changed the type of the states field (and the respective parameter and return types) from ArrayList<HashSet<NfaState>> to ArrayList<DfaState>
		2. Added an alphabet field to this class
		3. Created a static method makeDfa() to make and return a DFA based on the NFA parameter using the Subset Construction method

 */

package assignment;

import java.util.*;

public class Dfa {
	
	/*
	 *    Constants to define DFA characteristics of the DFA.
	 *      
	 *    SIGMA_LOWER and SIGMA_UPPER gives the Unicode codes for the first and last
	 *    characters in the input alphabet. As given, they represent the alphabet of 
	 *    the standard printable characters space (code 32) through ~ (code 126). 
	 *    But if e.g. you only wanted the characters A through E you could set
	 *    SIGMA_LOWER to 64 (code A) and SIGMA_UPPER to 68 (code E).
	 *    
	 *    MAX_STATES gives the maximum number of DFA states
	 *    
	 *    TRAP and START gives the row in the transition table for the trap and 
	 *    start states respectively
	 *    
	 *    ACCEPT and NON_ACCEPT are codes for accept (final) and non-accept states
	 *    respectively.
	 *    
	 *    For these constants to be useful, you should use them in your code rather 
	 *    than hardcode them.     
	 */	
	
    public static final int SIGMA_LOWER = 32;
    public static final int SIGMA_UPPER = 127;
    public static final int MAX_STATES  = 256;
    public static final int ACCEPT      = -1;
    public static final int START       = 1;
    public static final int TRAP        = 0;
    
    /*
     *  The structure of the transition table "transTable" is as follows
     *  
     *  transTable[i,0] gives for each state i, i = 0,1,... whether the state is 
     *       accepting or not. So it will have the value ACCEPT or NON_ACCEPT
     *  transTable[i,j] gives for each j = 1,2,... the next state of the DFA for each 
     *       symbol in the alphabet. The next state for the character with code 
     *       SIGMA_LOWER is stored at index j=1,the next state for the character with 
     *       code SIGMA_LOWER + 1  is stored at index j = 2, etc. 
     *  
     *  The trap state is 0 (TRAP = 0) so transTable[0,j] = 0 for j = 1,2,...
     *  
     *  The start state is 1 (START = 1)
     *  
     *  The List "states" holds as a Set of NfaStates the states from the nfa that make up 
     *  each dfa state.  Position i in the list holds the nfa states for dfa state i. 
     *  NB This is, of course, not necessary for using the dfa, but is merely for 
     *  information purposes
     *  
     */
    
    protected int[][]               transTable;
    protected ArrayList<DfaState>   states;
    protected int                   size;
    private Set<String>             alphabet;
    
    public Dfa() {
    }
     
    public Dfa(int[][] transTable, ArrayList<DfaState> states, int size) {
       this.transTable = transTable;
       this.states     = states;
       this.size       = size;        	
   }
    
   int[][] getTransTable() {
	   
	   return transTable;
   }
   
   int getSize()  {
	   
	   return size;
   }

	ArrayList<DfaState> getStates() {
      return states;
   }

	/**
	 * Use the Subset Construction to create a DFA based on the NFA parameter
	 * @param N
	 */
	public static void makeDfa(Nfa N) {

    	ArrayList<DfaState> dfaStates = new ArrayList<DfaState>(); //this is to store all the dfaStates for the DFA; will be set as the states field in the DFA object

    	/*
    	    The Trap State is unique to the DFA and doesnt contain any states from the NF (its nfaStateSet field is empty)
    	    There is a transition to this trap state when epsilon-closure(move(<DfaState>,<symbol>)) = {} (results in an empty set - no set of NFA states to transition to )
    	 */
    	HashSet<NfaState> trapStateSet = new HashSet<NfaState>(); //is currently empty and will remain so as we are not adding any elements to it
    	DfaState trapState = new DfaState(trapStateSet); //its stateNumber is 0 (== TRAP)
	    dfaStates.add(trapState);


	    Queue<NfaState> dfaStatesQueue = new LinkedList<NfaState>(); //this will be used to keep track of DFA states generated but still yet to be expanded

	    HashSet<NfaState> startStateSet = new HashSet<NfaState>();

	    //get ep-closure(NFAStartState)

	    DfaState startState = new DfaState(trapStateSet); //its stateNumber is 1 (== START)
		dfaStates.add(startState);

		while (!dfaStatesQueue.isEmpty()) {

		}



   }

}