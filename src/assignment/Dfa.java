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
		4. Created the static methods epsilonClosure() and move(), to be called in the makeDfa method()
		5. Created the static method getAlphabet() to be called in the makeDfa method()
		6. Changed SIGMA_LOWER and SIGMA_UPPER to non-static variables, so each DFA has its own values for it depending on its alphabet
		7. Created the static method getSigmaLowerAndUpper() to get the SIGMA_LOWER and SIGMA_UPPER of a specific alphabet



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


    public static final int MAX_STATES  = 256;
    public static final int ACCEPT      = -1;
    public static final int START       = 1;
    public static final int TRAP        = 0;

	//Changed SIGMA_LOWER and SIGMA_UPPER to non-static variables, so each DFA has its own values for it depending on its alphabet
	public int SIGMA_LOWER = 32;
	public int SIGMA_UPPER = 127;
    
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

	/**
	 *
	 * @param transTable a transition table for the dfa this is being assigned to
	 * @param states the states of the dfa this is being assigned to
	 * @param alphabet the alphabet of the dfa this is being assigned to
	 * @param size the size  of the dfa this is being assigned to
	 */
    public Dfa(int[][] transTable, ArrayList<DfaState> states, Set<Character> alphabet, int size) {
       this.transTable = transTable;
       this.states     = states;
       this.alphabet   = alphabet;
       this.size       = size;

	    int[] sigmaLU = getSigmaLowerAndUpper(alphabet);
	    this.SIGMA_LOWER = sigmaLU[0];
	    this.SIGMA_UPPER = sigmaLU[1];

	    this.trapState = this.states.get(0); //DfaState with stateNumber == 0 (which is at index 0) is the Trap State
	    this.startState = this.states.get(1); //DfaState with stateNumber == 1 (which is at index 1) is the Start State

	    this.acceptStates = new HashSet<DfaState>();

	    for (DfaState state: this.states) {
	    	if (state.isAcceptState()) {
	    		this.acceptStates.add(state);
		    }
	    }
   }

	/**
	 * @return the transition table for the dfa
	 */
	int[][] getTransTable() {
	   return transTable;
   }

	/**
	 *
	 * @return the number of states in the dfa
	 */
	int getSize()  {
	   return size;
   }


	/**
	 *
	 * @return the states in the dfa
	 */
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
		Set<Character> alpha = getAlphabet(n); //the alphabet for this DFA

		/*
			Column 0 is for whether the state is an ACCEPT State or a REJECT (non-ACCEPT) State
					-> The DfaState itself has a field and method to get whether it is an accept state or not, so we can use that for the conditional statement
						(That get method will be used to populate this column anyway, so IF we remove this column we can save execution time)
						(But its probably just better to just follow the design sir gave us)
		 */

		int[] sigmaLU = getSigmaLowerAndUpper(alpha);
		int sigmaLower = sigmaLU[0];
		int sigmaUpper = sigmaLU[1];

		int columns = sigmaUpper - sigmaLower + 2; // (sigmaUpper - sigmaLower + 1) for the character symbols then another +1 as the first column is for whether that State is an accept state or not
		int [][] transition = new int[MAX_STATES][columns]; //transition table


    	/*
    	    The Trap State is unique to the DFA and doesnt contain any states from the NFA (its nfaStateSet field is empty)
    	    There is a transition to this trap state when epsilon-closure(move(<DfaState>,<symbol>)) = {} (results in an empty set - no set of NFA states to transition to )
    	 */
    	HashSet<NfaState> trapStateSet = new HashSet<NfaState>(); //is currently empty and will remain so as we are not adding any elements to it
    	DfaState trapState = new DfaState(trapStateSet); //its stateNumber is 0 (== TRAP)
	    dfaStates.add(trapState); //index 0
	    size++;

	    for (int j = 0; j < columns; j++) {
	    	transition[TRAP][j] = 0;
	    }


	    Queue<DfaState> dfaStatesQueue = new LinkedList<DfaState>(); //this will be used to keep track of DFA states generated but still yet to be expanded

	    HashSet<NfaState> startStateSet = new HashSet<NfaState>();
	    startStateSet.add(n.start); //add the Start State of the NFA

	    startStateSet = epsilonClosure(startStateSet);

	    DfaState startState = new DfaState(startStateSet); //its stateNumber is 1 (== START)
		dfaStates.add(startState); //index 1
		size++; //increment the number of DFA states we have created thus far

		dfaStatesQueue.add(startState);

		if (startState.isAcceptState()) {
			transition[START][0] = ACCEPT;
		}


		while (!dfaStatesQueue.isEmpty()) {
			DfaState T = dfaStatesQueue.remove();

			for (Character a : alpha){
				HashSet<NfaState> nfaStateSet = epsilonClosure((move(T.getNfaStateSet(), a)));// should be a single character - get first
				DfaState V = new DfaState(nfaStateSet);

				//There is a transition to this trap state when epsilon-closure(move(<DfaState>,<symbol>)) = {} (results in an empty set - no set of NFA states to transition to )
				if (V.getNfaStateSet().isEmpty()) {
					int symbolCol = (int)a - sigmaLower + 1; //+1 because column at index 0 is for whether the state is an Accept State or not
					transition[dfaStates.indexOf(T)][symbolCol] = TRAP;
					continue; //dont execute the code below for this char, but go to the next char and start again this loop for it
				}

				if (!dfaStates.contains(V)) { //will call the equals() method in the DfaState class
					dfaStates.add(V);
					size++;
					dfaStatesQueue.add(V);

					if (V.isAcceptState()) {
						transition[dfaStates.indexOf(V)][0] = ACCEPT;
					}

					if (V.isAcceptState()){
						transition[dfaStates.indexOf(V)][0] = ACCEPT;
					}

				} else { //if the same DFA State as V is already created.. [2 DFA States are equal if they are made up of the same set of NFA States]
					//get the index of the DfaState that contains the same NFA States set as V
					int index = dfaStates.indexOf(V); //will call the equals() method in the DfaState class
					V = dfaStates.get(index); // V points to that DFA State already created that is the same as it
				}

				int symbolCol = (int)a - sigmaLower + 1; //+1 because column at index 0 is for whether the state is an Accept State or not

				transition[dfaStates.indexOf(T)][symbolCol] = dfaStates.indexOf(V);
			}
		}

		return new Dfa(transition, dfaStates, alpha, size);
   }


	/**
	 * @param nfaStates the set of nfa states that represent the single dfa state
	 * @return the set of all states reachable on epsilon transitions from each individual NFA State in the nfaStates set
	 */
	public static HashSet<NfaState> epsilonClosure(HashSet<NfaState> nfaStates) {

		HashSet<NfaState> epsilonClosure = (HashSet<NfaState>) nfaStates.clone();
		//...
		Stack<NfaState> epsilonStatesStack = new Stack<NfaState>();
		for (NfaState n : nfaStates){
			/*
			getNext1() corresponds to getSymbol()
			getNext2() is a transition on epsilon
			i.e. you transition from current state to getNext1() on getSymbol1() (or range [getSymbol1(), getSymbol2()]
			 */

			/*
			// NFA created such that states null by default and symbol2 set to epsilon => check if both there is a next state and if you transition to it on epsilon
			if (n.getNext1() != null && n.getSymbol() == NfaState.EPSILON ) epsilonStates.push(n.getNext1());
			if (n.getNext2() != null && n.getSymbol2() == NfaState.EPSILON ) epsilonStates.push(n.getNext2());

				// while you can still find epsilon transitions for this specific path


				while (!epsilonStates.empty()){
					NfaState nextState = epsilonStates.pop();
					ec.add(nextState);
					if (nextState.getNext1() != null && nextState.getSymbol() == NfaState.EPSILON) epsilonStates.push(nextState.getNext1());
					if (nextState.getNext2() != null && nextState.getSymbol2() == NfaState.EPSILON) epsilonStates.push(nextState.getNext1());
				}

				*/

			epsilonStatesStack.push(n);

			while (!epsilonStatesStack.empty()) {
				NfaState t = epsilonStatesStack.pop();

				//Because of Thompson's Construction, NfaState t has a max of 2 epsilon transitions from it

				if (t.getNext1() != null && t.getSymbol() == NfaState.EPSILON) {
					epsilonClosure.add(t.getNext1()); //add() will first check if next2 is already in it or not
					epsilonStatesStack.push(t.getNext1());
				}

				if (t.getNext2() != null && t.getSymbol2() == NfaState.EPSILON) {
					epsilonClosure.add(t.getNext2()); //add() will first check if next2 is already in it or not
					epsilonStatesStack.push(t.getNext2());
				}
			}


		}
		return epsilonClosure;
	}


	/**
	 * @param nfaStates
	 * @param symbol
	 * @return the set of all states reachable by transitioning on <symbol> from each individual NFA State in the nfaStates set
	 */
	public static HashSet<NfaState> move(HashSet<NfaState> nfaStates, char symbol) {

		HashSet<NfaState> move = new HashSet<NfaState>();
		//...
		for (NfaState n : nfaStates){
			NfaState nextState = n.getNext1(); // next1 only because next2 is either null or epsilon, doesn't count for this stage
			/*
			getNext1() corresponds to getSymbol()
			i.e. you transition from current state to getNext1() on getSymbol1() (or range [getSymbol1(), getSymbol2()]
			 */
			if (nextState != null) {
				//if (nextState.getSymbol2() == NfaState.EPSILON) { //the transition is on a single transition
				if (n.getSymbol2() < n.getSymbol()) { //the transition is on a single transition
					if (n.getSymbol() == symbol) {
						move.add(nextState);
					}
				} else { //The transition is on a character class
					if (symbol >= n.getSymbol() && symbol <= n.getSymbol2()) {
						move.add(nextState);
					}
				}
			}


		}
		return move;
	}


	/**
	 * @param n The original nfa we wish to convert to a dfa
	 * @return the alphabet of the regular language the nfa describes
	 */
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
			char symbol2 = state.symbol2;

			if (symbol != NfaState.ACCEPT && symbol != NfaState.EPSILON) {
				alphabet.add(symbol); //add() first checks to see if the element is already in the set or not

				if (symbol2 > symbol) { //if transitioning on a character class
					int symbolASCII = (int) symbol;
					int symbol2ASCII = (int) symbol2;

					//For the remaining characters in the character class, add it to the alphabet
					for (int a = symbolASCII+1; a <= symbol2ASCII; a++) {
						char c = (char) a;
						alphabet.add(c);

					}
				}
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

	/**
	 * @param alphabet The alphabet of the NFA
	 * @return the highest and lowest unicode values for the alphabet
	 */

	public static int[] getSigmaLowerAndUpper(Set<Character> alphabet){

		int sigmaLower = 127; //initialise low to highest value
		int sigmaUpper = 32;  //initialise high to lowest value

		for (Character c: alphabet) {
			int cAsInt = (int) c;

			if (cAsInt < sigmaLower) {
				sigmaLower = cAsInt;
			}

			if (cAsInt > sigmaUpper) {
				sigmaUpper = cAsInt;
			}
		}

		int[] sigLU = {sigmaLower, sigmaUpper}; //sigLU[0] = sigmaLower, sigLU[1] = sigmaUpper
		return  sigLU;
	}

}