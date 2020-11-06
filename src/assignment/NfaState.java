package assignment;

/*
 * Class representing a single state in an NFA.
 *
 * It is assumed that the NFA is constructed using Thompson's construction
 * so the resultant NFA has a particular form.
 *
 *	(i)		a single final state
 *	(ii)	each state has 1 outgoing transition on a character, or range 
 *			of characters in a character class, or 1 or 2 outgoing 
 *			transitions on epsilon
 */

public class NfaState {
	
	public static final char EPSILON         = 0,         // Character code for epsilon
	                         ACCEPT          = 1;         // Character code for final state
	                        
	
	                        	
	protected NfaState next1;      // Only transition on character or 1st 
	                               // transition on epsilon
	protected NfaState next2;      // 2nd transition on epsilon if there is 
	                               // one.  In all other cases, null. 
	protected char     symbol;     // Transition character or lower limit 
	                               // transition character in character class
	protected char     symbol2;    // Upper limit transition character in 
	                               // class, or epsilon if single character
	protected int      stateNo;    // Unique identifying state number
	
	/*
	 * Default constructor 
	 */
	public NfaState() {		
	}
	
	/*
	 * Construct a new NFA state for a single character or epsilon transition 
	 */
	public NfaState(NfaState next1,
	                NfaState next2,
	                char     symbol,
	                int      stateNo) {
	             	
	     this.next1   = next1;
	     this.next2   = next2;
	     this.symbol  = symbol;
	     this.symbol2 = EPSILON;
	     this.stateNo = stateNo;             	
	}
	
	/*
	 * Construct a new NFA state for a character class transition
	 */
	public NfaState(NfaState next1,
	                NfaState next2,
	                char     symbol,
	                char     symbol2,
	                int      stateNo) {
	             	
	     this.next1   = next1;
	     this.next2   = next2;
	     this.symbol  = symbol;
	     this.symbol2 = symbol2;
	     this.stateNo = stateNo;             	
	}
	
	/*
	 *  Getters and setters for instance variables
	 */
	public NfaState getNext1() {
		
		return next1;
	}
	
	public NfaState getNext2() {
		
		return next2;
	}
	
	public char getSymbol() {
		
		return symbol;
	}
	
	public char getSymbol2() {
		
		return symbol2;
	}
	
	public int getStateNo() {
		
		return stateNo;
	}
	
	public void setNext1(NfaState next1) {
		
		this.next1 = next1;
	}
	
	public void setNext2(NfaState next2) {
		
		this.next2 = next2;
	}
	
	public void setSymbol(char symbol) {
		
		this.symbol = symbol;
	}
	
	public void setSymbol2(char symbol2) {
		
		this.symbol2 = symbol2;
	}
	
	public void setStateNo(int stateNo) {
		
		this.stateNo = stateNo;
	}
	
	/*
	 * Return a string representation of this NFA state
	 */
	public String toString() {
		
		String s = stateNo + "\t: [";
		switch (symbol) {
		   case EPSILON : s += "epsilon";
		                  break;
		                  
		   case ACCEPT  : s += "final";
		                  break;
		                  
		   default      : s += symbol;
		                  if (symbol2 > symbol)
		                    s += "-" + symbol2;		                  
		}
		s += "," + (next1 == null ? "-" : next1.getStateNo());
		s += "," + (next2 == null ? "-" : next2.getStateNo()) + "]"; 
		return s;  	 
	}	             
}