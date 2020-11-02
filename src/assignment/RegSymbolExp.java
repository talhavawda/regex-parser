package assignment;

/*
 *  Class representing a single character tree in a RegExp expression tree.
 */
public class RegSymbolExp extends RegExp {
	
	protected char symbol;    // character represented
	
	
	public RegSymbolExp(char symbol) {
		
		this.symbol = symbol;				
	}

     /*
	 * Make and return a Nfa for this character expression tree
	 */	
	public Nfa makeNfa() {
		
		int startNum = getNextStateNum();
		NfaState accept = new NfaState(null,null,NfaState.ACCEPT,getNextStateNum());
		NfaState start  = new NfaState(accept,null,symbol,startNum);
		return new Nfa(start,accept,2);
	}

	/*
	 *  Decompile this character expression tree back to its original
	 *  form as a string
	 */	
	public String decompile() {
		
		return "\"" + symbol + "\"";
	}
}