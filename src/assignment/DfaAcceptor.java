package assignment;

public class DfaAcceptor {

	/*
		Constructor - take in as parameter a Dfa object and store it as a field

		define a boolean method accept() with a string parameter that determines if the Dfa should accept the string or not
			->> SEE THE DFA-RECOGNISE ALGORITHM IN THE COMP314 SLIDES
			->> this method should use the transition table of the Dfa object to determine if the string must be accepted or ont
				->> Study the DfaState and Dfa class (and also the Notes.txt file I made earlier) to understand how it works
					->> Divya and I are not entirely completed with the code, but assume that everything works when coding this class
						i.e. The transition table is created correctly etc.

						q  = s0
    x = 1st input symbol
    while x ≠  eof  {
         q  =  δ(q,x)
         x  =  next input symbol
     }
     if q ∈ F
         input recognised
     else
         input not recognised	 */
	public Dfa dfa;
	protected int stateAt=1;
	public DfaAcceptor(Dfa dfa){
		this.dfa=dfa;
	}

	public boolean accept(String input){
		for(int i=0; i<input.length(); i++){
			stateAt=dfa.transTable[stateAt][input.charAt(i)]; //stateAt is initially set to start state
			if(stateAt==0){
				return false; // checks if its in trap state
			}
		}

		return stateAt == Dfa.ACCEPT;  //if (stateAt == Dfa.ACCEPT) then return true, else return false

	}
}
