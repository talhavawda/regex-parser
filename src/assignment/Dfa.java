/*
	This class was given.
	We are allowed to use our own design for this class (i.e. can modify or restructure as we wish)

	What we changed:
		1. Created a DfaState class to represent a set of NFA States (See DfaState.java)
			-> Thus changed the type of the states field (and the respective parameter and return types) from ArrayList<HashSet<NfaState>> to ArrayList<DfaState>
		2. Added the following  fields to this class: alphabet, trapState, startState, acceptStates
			-> Just added them to have the fields to represent the 5-tuple formal definition of a DFA
				-> We may or may not use some of these fields as some of these properties can be obtained from each DfaState themself
		3. Created a static method makeDfa() to make and return a DFA based on the NFA parameter using the Subset Construction method
			Todo - finish coding this method
		4. Created the static methods epsilonClosure() and move(), to be called in the makeDfa method()
			Todo - code these methods
		5. Created the static method getAlphabet() to be called in the makeDfa method()
		Todo - character class

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
     *  each dfa state (a DfaState).  Position i in the list holds the nfa states for dfa state i.
     *  NB This is, of course, not necessary for using the dfa, but is merely for 
     *  information purposes
     *
     * *** DFA State i in this 'states' field corresponds to the state i in transTable[i, j]
     * *** DFA State i in this 'states' field should have its 'stateNumber' field as i
     *  
     */

	protected ArrayList<DfaState>   states;
	private Set<Character>          alphabet; //Made the BaseType String as it wont accept the primitive char type
	protected int[][]               transTable;
	private DfaState                startState;
	private Set<DfaState>           acceptStates;

	private DfaState                trapState;
    protected int                   size; //the actual number of rows (states) in this DFA -> so dont have to traverse till MAX_STATES



    public Dfa() {
    }

    //Todo - add alphabet parameter [DONE, reorder the parameter list]
    public Dfa(int[][] transTable, ArrayList<DfaState> states, Set<Character> alphabet, int size) {
       this.transTable = transTable;
       this.states     = states;
	    this.alphabet   = alphabet;
       this.size       = size;


	    this.trapState = this.states.get(0); //DfaState with stateNumber == 0 (which is at index 0) is the Trap State
	    this.startState = this.states.get(1); //DfaState with stateNumber == 1 (which is at index 1) is the Start State

	    this.acceptStates = new HashSet<DfaState>();

	    for (DfaState state: this.states) {
	    	if (state.isAcceptState()) {
	    		this.acceptStates.add(state);
		    }
	    }
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
	 * @param n - an NFA (which was created using Thompson's Construction)
	 */
	public static Dfa makeDfa(Nfa n) {

		int size = 0;
    	ArrayList<DfaState> dfaStates = new ArrayList<DfaState>(); //this is to store all the dfaStates for the DFA; will be set as the states field in the DFA object

		/*
			adding extra column for whether the state is ACCEPT or NON_ACCEPT state (this is col 0)
				-> Todo - this is based on the design sir gave. we can change this design by removing this column 0
					-> The DfaState itself has a field and method to get whether it is an accept state or not, so we can use that for the conditional statement
						(That get method will be used to populate this column anyway, so IF we remove this column we can save execution time)
						(But its probably just better to just follow the design sir gave us)
		 */
		int columns = SIGMA_UPPER - SIGMA_LOWER + 2; // ( SIGMA_UPPER - SIGMA_LOWER + 1) for the character symbols then another +1 as the first column is for whether that State is an accept state or not
		int [][] transition = new int[MAX_STATES][columns]; //transition table


    	/*
    	    The Trap State is unique to the DFA and doesnt contain any states from the NFA (its nfaStateSet field is empty)
    	    There is a transition to this trap state when epsilon-closure(move(<DfaState>,<symbol>)) = {} (results in an empty set - no set of NFA states to transition to )
    	 */
    	HashSet<NfaState> trapStateSet = new HashSet<NfaState>(); //is currently empty and will remain so as we are not adding any elements to it
    	DfaState trapState = new DfaState(trapStateSet); //its stateNumber is 0 (== TRAP)
	    dfaStates.add(trapState);
	    size++;

	    for (int j = 0; j < columns; j++) {
	    	transition[TRAP][j] = 0;
	    }


	    Queue<DfaState> dfaStatesQueue = new LinkedList<DfaState>(); //this will be used to keep track of DFA states generated but still yet to be expanded

	    HashSet<NfaState> startStateSet = new HashSet<NfaState>();
	    startStateSet.add(n.start); //add the Start State of the NFA

	    startStateSet = epsilonClosure(startStateSet);

	    DfaState startState = new DfaState(startStateSet); //its stateNumber is 1 (== START)
		dfaStates.add(startState);
		size++; //increment the number of DFA states we have created thus far

		dfaStatesQueue.add(startState);

		if (startState.isAcceptState()) {
			transition[START][0] = ACCEPT;
		}

		Set<Character> alpha = getAlphabet(n); //the alphabet for this DFA
		//Todo - SEE SUBSET CONSTRUCTION ALGORITHM IN SLIDES (the mark V as final is already done in the DfaState class)

		while (!dfaStatesQueue.isEmpty()) {
			DfaState T = dfaStatesQueue.remove();

			for (Character a : alpha){
				HashSet<NfaState> V = epsilonClosure((move(T.getNfaStateSet(), a)));// should be a single character - get first

				//Todo - if epsilonClosure() is empty then transition to trap State

				if (!dfaStates.contains(V)) {
					DfaState d = new DfaState(V);
					dfaStates.add(d);
					size++;

					dfaStatesQueue.add(d);
					/*for (NfaState v : V){


					}*/

				}
				transition[dfaStates.indexOf(T)][a] = dfaStates.indexOf(V);
			}
		}


		Dfa d = new Dfa();
		//...

		return d;
   }

	//TODO
	/**
	 * @return the set of all states reachable on epsilon transitions from each individual NFA State in the nfaStates set
	 */
	public static HashSet<NfaState> epsilonClosure(HashSet<NfaState> nfaStates) {

		HashSet<NfaState> ec = (HashSet<NfaState>) nfaStates.clone();
		//...
		Stack<NfaState> epsilonStates = new Stack<NfaState>();
		for (NfaState n : nfaStates){
			// if there are transitions and if any of the transitions are on epsilon
			if (n.getNext1() != null && n.getNext1().getSymbol() == NfaState.EPSILON || n.getNext2() != null && n.getNext2().getSymbol() == NfaState.EPSILON ) {
				if (n.getNext1() != null && n.getNext1().getSymbol() == NfaState.EPSILON ) epsilonStates.push( n.getNext1());
				if (n.getNext2() != null && n.getNext2().getSymbol() == NfaState.EPSILON ) epsilonStates.push(n.getNext2());
				// while you can still find epsilon transitions for this specific path
				while (!epsilonStates.empty()){
					NfaState nextState = epsilonStates.pop();
					ec.add(nextState);
					if (nextState.getNext1() != null && nextState.getNext1().getSymbol() == NfaState.EPSILON) epsilonStates.push(nextState.getNext1());
					if (nextState.getNext2() != null && nextState.getNext2().getSymbol() == NfaState.EPSILON) epsilonStates.push(nextState.getNext1());
				}

			}


		}
		return ec;
	}

	//TODO
	/**
	 * @param nfaStates
	 * @param symbol
	 * @return the set of all states reachable by transitioning on <symbol> from each individual NFA State in the nfaStates set
	 */
	public static HashSet<NfaState> move(HashSet<NfaState> nfaStates, char symbol) {

		HashSet<NfaState> move = new HashSet<NfaState>();
		//...
		for (NfaState n : nfaStates){
			NfaState nextState = n.getNext1(); // next 1 only bc next2 is either null or epsilon
			if (nextState != null && nextState.getSymbol() == symbol) move.add(nextState);

		}
		return move;
	}


	public static Set<Character> getAlphabet(Nfa n) {

		//Made the BaseType String as it wont accept the primitive char type
		Set<Character> alphabet = new HashSet<Character>();

		Stack<NfaState> states = new Stack<NfaState>();
		HashSet<NfaState> statesChecked = new HashSet<NfaState>();

		states.push(n.start);
		statesChecked.add(n.start);

		while (!states.empty()) {

			NfaState state = states.pop();
			char symbol = state.symbol;

			if (symbol != NfaState.ACCEPT && symbol != NfaState.EPSILON) {
				alphabet.add(symbol); //add() first checks to see if the element is already in the set or not
			}

			if (state.next1 != null && !statesChecked.contains(state.next1)) {
				states.push(state.next1);
				statesChecked.add(state.next1);
			}

			if (state.next2 != null && !statesChecked.contains(state.next2)) {
				states.push(state.next2);
				statesChecked.add(state.next2);
			}
		}

		return alphabet;
	}

}