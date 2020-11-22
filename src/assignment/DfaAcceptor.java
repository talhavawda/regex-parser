package assignment;

/**
 * A class to determine if a string belongs to a particular language
 * i.e. Determine if the DFA that represents this language accepts or rejects the string
 *
 * The class has a single field that is the Dfa for the language (it is passed in via the constructor)
 * It has a boolean method accept() that takes in a string as a parameter and determines if the DFA should accept or reject the string
 * i.e. it returns true if the DFA accepts the string, otherwise returns false
 */
public class DfaAcceptor {

	private Dfa dfa;


	/**
	 * Constructor
	 * @param dfa - the DFA representing the language
	 */
	public DfaAcceptor(Dfa dfa){
		this.dfa=dfa;
	}

	/*
		 Takes in a string as a parameter and determines if the DFA should accept or reject this string

		 It uses the transition table of the Dfa object to determine if the string must be accepted or rejected

		 DFA Accepting Algorithm:

			q  = s0 (Start State)
	        x = 1st input symbol from input string

	        while x ≠  end of input string {
	             q  =  δ(q,x)
	             x  =  next input symbol
	         }

	         if q ∈ F (If q is an Accept State)
	             input recognised (Accept the string)
	         else
	             input not recognised (Reject the string)
	 */
	public boolean accept(String input){

		int stateAt = Dfa.START; //stateAt is initially set to the Start State

		for (int i = 0; i < input.length(); i++){ //traverse through the input string - each input symbol at a time

			char symbol = input.charAt(i);
			int symbolASCII = (int) symbol;

			if (symbolASCII < dfa.SIGMA_LOWER || symbolASCII> dfa.SIGMA_UPPER ) {
				return false; //the symbol is not part of the alphabet defined by the language that this DFA represents, so REJECT the input string
			}

			int symbolCol = symbolASCII - dfa.SIGMA_LOWER + 1; //+1 because column at index 0 is for whether the state is an Accept State or not
			stateAt = dfa.transTable[stateAt][symbolCol]; //transition to the next state based on the current input symbol

			if(stateAt == Dfa.TRAP) { // checks if its in trap state
				return false; //Reject the input string
			}

		}

		//We have now finished reading in the input

		return dfa.transTable[ stateAt ][0]== Dfa.ACCEPT;  //if the last state we in is an Accept State then return true, else return false - ACCEPT states are flagged by the value of the first column in the transition table

	}
}
