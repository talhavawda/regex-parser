package assignment;


/*
 *  Class representing a operator tree in a RegExp expression tree.
 */
 public class RegOpExp extends RegExp{
	
	// Codes for different operator types
	public static final int ALTERNATION      = 0,
	                        CONCATENATION    = 1,
	                        KLEENE_CLOSURE   = 2,
	                        POSITIVE_CLOSURE = 3,
	                        OPTION           = 4;
	                  
	protected RegExp left;    // Left operand
	protected int    op;      // Operator
	protected RegExp right;   // Right operand (null if Operator is unary)
	
	/*
	 * Construct a unary operator expression tree
	 */
	public RegOpExp(RegExp left,
	                int    op) {
		
		this(left,op,null);
	}
	
	/*
	 * Construct a binary operator expression tree
	 */
	public RegOpExp(RegExp left,
	                int    op,
	                RegExp right) {
	              	
	    this.left  = left;
	    this.op    = op;
	    this.right = right;      	
	}

	/*
	 * Make and return a Nfa for this operator expression tree
	 */	
	public Nfa makeNfa() {
		
		switch (op) {
			case ALTERNATION : 
			      return makeAlternation(left.makeNfa(),right.makeNfa());
			      
			case CONCATENATION : 
			     return makeConcatenation(left.makeNfa(),right.makeNfa());
			     
			case KLEENE_CLOSURE :
			     return makeKleeneClosure(left.makeNfa());
			     
			case POSITIVE_CLOSURE :
			     return makePositiveClosure(left.makeNfa());
			     
			case OPTION :
			     return makeOption(left.makeNfa());	
			     		     			     
			default :
			     throw new RuntimeException("Illegal operator");		     
			
		}
	}
	
	/*
	 * Make and return a Nfa for a new alternation operator expression tree
	 */
	private Nfa makeAlternation(Nfa n1,      // Left Nfa alternated with
	                            Nfa n2) {    // Right Nfa
	                            	
	                            	
	     NfaState start = new NfaState(n1.getStart(),n2.getStart(),
	                                   NfaState.EPSILON,getNextStateNum());
	     NfaState accept = new NfaState(null,null,NfaState.ACCEPT,
	                                    getNextStateNum());
	     n1.getAccept().setSymbol(NfaState.EPSILON);
	     n1.getAccept().setNext1(accept);
	     n2.getAccept().setSymbol(NfaState.EPSILON);
	     n2.getAccept().setNext1(accept);
	     return new Nfa(start,accept,n1.getNumStates() + n2.getNumStates() + 2); 	     
	}

	/*
	 * Make and return a Nfa for a new concatenation operator expression tree
	 */	
	private Nfa makeConcatenation(Nfa n1,
	                              Nfa n2) {
	     
	     n1.getAccept().setNext1(n2.getStart().getNext1()); 
         n1.getAccept().setNext2(n2.getStart().getNext2());
         n1.getAccept().setSymbol(n2.getStart().getSymbol());  
         n1.setAccept(n2.getAccept());  
         n1.setNumStates(n1.getNumStates() + n2.getNumStates() - 1);                              	
	     return n1;                       	
	}

	/*
	 * Make and return a Nfa for a new Kleene closure operator expression tree
	 */	
	private Nfa makeKleeneClosure(Nfa n) {
	     
		 int startNum = getNextStateNum();
	     NfaState accept = new NfaState(null,null,NfaState.ACCEPT,
	                                    getNextStateNum());
	     NfaState start  = new NfaState(n.getStart(),accept,
	                                    NfaState.EPSILON,startNum);
	     n.getAccept().setSymbol(NfaState.EPSILON);
	     n.getAccept().setNext1(accept);
	     n.getAccept().setNext2(n.getStart());
	     return new Nfa(start,accept,n.getNumStates() + 2);	                           	
	}

	/*
	 * Make and return a Nfa for a new positive closure oerator expression tree
	 */	
	private Nfa makePositiveClosure(Nfa n) {
	       
		 int startNum = getNextStateNum();
	     NfaState accept = new NfaState(null,null,NfaState.ACCEPT,
	                                    getNextStateNum());
	     NfaState start  = new NfaState(n.getStart(),null,
	                                    NfaState.EPSILON,startNum);
	     n.getAccept().setSymbol(NfaState.EPSILON);
	     n.getAccept().setNext1(accept);
	     n.getAccept().setNext2(n.getStart());	     
	     return new Nfa(start,accept,n.getNumStates() + 2);	                           	
	}

	/*
	 * Make and return a Nfa for a new option operator expression tree
	 */	
	private Nfa makeOption(Nfa n) {
	      
		 int startNum = getNextStateNum();
	     NfaState accept = new NfaState(null,null,NfaState.ACCEPT,
	                                    getNextStateNum());
	     NfaState start  = new NfaState(n.getStart(),accept,
	                                    NfaState.EPSILON,startNum);
	     n.getAccept().setSymbol(NfaState.EPSILON);
	     n.getAccept().setNext1(accept);
	     return new Nfa(start,accept,n.getNumStates() + 2);	                           	
	}

	/*
	 *  Decompile this operator expression tree back to its original
	 *  form as a string
	 */	
	public String decompile() {
		
		String leftString  = "(" + left.decompile();
		String rightString = ")";
		if (right != null)
		  rightString = right.decompile() + rightString;
		
		switch (op)  {
			case ALTERNATION      : return leftString + "|" + rightString;
			                        
		    case CONCATENATION    : return leftString + "." + rightString;
		    
		    case OPTION			  : return leftString + rightString + "?";
		    
		    case KLEENE_CLOSURE   : return leftString + rightString + "*";
		    
		    case POSITIVE_CLOSURE : return leftString + rightString + "+";
		    
		    default               : throw new RuntimeException
		                                ("Illegal operator in RegOpExp");
		}
		  
	}

}